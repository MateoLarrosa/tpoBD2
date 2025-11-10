CREATE KEYSPACE IF NOT EXISTS tpdb_keyspace 
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};