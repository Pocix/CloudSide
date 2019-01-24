create table `gateway_api_define` (
  `id` varchar(50) not null,
  `path` varchar(255) not null,
  `service_id` varchar(50) default null,
  `url` varchar(255) default null,
  `retryable` tinyint(1) default null,
  `enabled` tinyint(1) not null,
  `strip_prefix` int(11) default null,
  `api_name` varchar(255) default null,
  primary key (`id`)
) engine=innodb default charset=utf8


INSERT INTO `gateway_api_define` VALUES ('api-a', '/api-a/**', 'Test1-service', 'http://192.168.30.186:2222', 0, 1, 1, NULL);
INSERT INTO `gateway_api_define` VALUES ('api-a-swagger', '/api-a-swagger/**', 'Test1-service', 'http://192.168.30.186:2222', 0, 1, 1, NULL);
INSERT INTO `gateway_api_define` VALUES ('api-b', '/api-b/**', 'Test2-service', 'http://192.168.30.186:2232', 0, 1, 1, NULL);
INSERT INTO `gateway_api_define` VALUES ('ms', '/monitor-service/**', 'monitor-service', 'http://192.168.30.186:1112', 0, 1, 1, NULL);