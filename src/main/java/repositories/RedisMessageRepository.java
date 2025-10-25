// package repositories;

// import connections.RedisPool;
// import exceptions.ErrorConectionMongoException;
// import modelo.Mensaje;

// import redis.clients.jedis.StreamEntryID;
// import redis.clients.jedis.Jedis;
// import redis.clients.jedis.StreamEntry;
// import redis.clients.jedis.StreamEntryIDGroup;
// import redis.clients.jedis.params.XAddParams;
// import redis.clients.jedis.params.XReadGroupParams;
// import redis.clients.jedis.resps.StreamEntryList;

// import java.util.*;
// import java.util.stream.Collectors;

// public class RedisMessageRepository {

//     private final RedisPool pool;

//     public RedisMessageRepository(RedisPool pool) {
//         this.pool = pool;
//     }

//     // XADD
//     public String appendMessage(String chatKey, String remitenteId, String contenido) throws ErrorConectionMongoException {
//         return pool.execute(jedis -> {
//             Map<String, String> fields = new HashMap<>();
//             fields.put("remitente", remitenteId);
//             fields.put("contenido", contenido);
//             StreamEntryID id = jedis.xadd(chatKey, XAddParams.xAddParams().id(StreamEntryID.NEW_ENTRY), fields);
//             return id.toString();
//         });
//     }

//     // XREVRANGE chatKey + - COUNT n  (últimos n mensajes, más nuevo primero)
//     public List<Mensaje> getLastMessages(String chatKey, int count) throws ErrorConectionMongoException {
//         return pool.execute(jedis -> {
//             List<StreamEntry> entries = jedis.xrevrange(chatKey, "+", "-", count);
//             return entries.stream()
//                     .map(e -> Mensaje.fromStreamEntry(chatKey, e.getID().toString(), e.getFields()))
//                     .collect(Collectors.toList());
//         });
//     }

//     // Crear consumer group si no existe (XGROUP CREATE mkstream)
//     public void ensureConsumerGroup(String chatKey, String groupName) throws ErrorConectionMongoException {
//         pool.execute((Jedis jedis) -> {
//             try {
//                 // Intentamos crear; si existe, lanzará excepción -> la ignoramos.
//                 jedis.xgroupCreate(chatKey, groupName, StreamEntryID.LAST_ENTRY, true); // mkstream=true
//             } catch (Exception ignored) {
//             }
//             return null;
//         });
//     }

//     // Lectura “en tiempo real” para un consumer del grupo (long-polling)
//     // Devuelve hasta 'count' mensajes nuevos, bloqueando 'blockMs' si no hay.
//     public List<Mensaje> readNewMessages(String chatKey, String groupName, String consumer, int count, long blockMs)
//             throws ErrorConectionMongoException {

//         return pool.execute(jedis -> {
//             Map.Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<>(chatKey, StreamEntryID.UNRECEIVED_ENTRY);
//             List<StreamEntryList> result = jedis.xreadGroup(groupName, consumer,
//                     XReadGroupParams.xReadGroupParams().count(count).block(blockMs),
//                     Collections.singletonList(stream));

//             if (result == null || result.isEmpty()) {
//                 return Collections.emptyList();
//             }

//             List<Mensaje> mensajes = new ArrayList<>();
//             for (StreamEntryList sel : result) {
//                 for (StreamEntry e : sel.getEntries()) {
//                     mensajes.add(Mensaje.fromStreamEntry(chatKey, e.getID().toString(), e.getFields()));
//                 }
//             }


