package repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import connections.RedisPool;
import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.resps.StreamEntry;

public class RedisMessageRepository {

    private final RedisPool pool;
    private static RedisMessageRepository instance;

    private RedisMessageRepository() {
        this.pool = RedisPool.getInstance();
    }

    public static RedisMessageRepository getInstance() {
        if (instance == null) {
            instance = new RedisMessageRepository();
        }
        return instance;
    }

    public String appendMessage(String chatKey, String remitenteId, String contenido) throws ErrorConectionMongoException {
        return pool.execute(jedis -> {
            Map<String, String> fields = new HashMap<>();
            fields.put("remitente", remitenteId);
            fields.put("contenido", contenido);
            StreamEntryID id = jedis.xadd(chatKey, XAddParams.xAddParams().id(StreamEntryID.NEW_ENTRY), fields);
            return id.toString();
        });
    }

    public List<Mensaje> getLastMessages(String chatKey, int count) throws ErrorConectionMongoException {
        return pool.execute(jedis -> {
            List<StreamEntry> entries = jedis.xrevrange(chatKey, "+", "-", count);
            return entries.stream()
                    .map(e -> Mensaje.fromStreamEntry(chatKey, e.getID().toString(), e.getFields()))
                    .collect(Collectors.toList());
        });
    }

    public void ensureConsumerGroup(String chatKey, String groupName) throws ErrorConectionMongoException {
        pool.execute((Jedis jedis) -> {
            try {
                jedis.xgroupCreate(chatKey, groupName, StreamEntryID.LAST_ENTRY, true);
            } catch (Exception ignored) {
            }
            return null;
        });
    }

    public List<Mensaje> readNewMessages(String chatKey, String groupName, String consumer, int count, int blockMs) throws ErrorConectionMongoException {

        return pool.execute(jedis -> {
            XReadGroupParams params = XReadGroupParams.xReadGroupParams()
                    .count(count)
                    .block(blockMs);  // <- int

            Map<String, StreamEntryID> streams
                    = Collections.singletonMap(chatKey, StreamEntryID.UNRECEIVED_ENTRY);

            List<Map.Entry<String, List<redis.clients.jedis.resps.StreamEntry>>> result
                    = jedis.xreadGroup(groupName, consumer, params, streams);

            if (result == null || result.isEmpty()) {
                return Collections.emptyList();
            }

            List<Mensaje> out = new ArrayList<>();
            for (Map.Entry<String, List<redis.clients.jedis.resps.StreamEntry>> stream : result) {
                for (redis.clients.jedis.resps.StreamEntry e : stream.getValue()) {
                    out.add(Mensaje.fromStreamEntry(
                            chatKey,
                            e.getID().toString(),
                            e.getFields()
                    ));
                }
            }
            return out;
        });
    }

    public long ackMessages(String chatKey, String groupName, List<String> ids) throws ErrorConectionMongoException {
        return pool.execute(jedis -> jedis.xack(chatKey, groupName, ids.stream().map(StreamEntryID::new).toArray(StreamEntryID[]::new)));
    }
}
