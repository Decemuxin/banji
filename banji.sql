-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: clazz_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clazz`
--

DROP TABLE IF EXISTS `clazz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clazz` (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_code` varchar(20) DEFAULT NULL,
  `class_name` varchar(50) DEFAULT NULL,
  `grade` int DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  `head_teacher` varchar(30) DEFAULT NULL,
  `establish_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clazz`
--

LOCK TABLES `clazz` WRITE;
/*!40000 ALTER TABLE `clazz` DISABLE KEYS */;
/*!40000 ALTER TABLE `clazz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int NOT NULL COMMENT '学生ID',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  `exam_type` varchar(20) DEFAULT '期末' COMMENT '考试类型：期中/期末/补考',
  `exam_date` date DEFAULT NULL COMMENT '考试日期',
  `semester` varchar(20) DEFAULT NULL COMMENT '学期（如：2025-2026-1）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成绩表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (1,1,'Java程序设计',85.50,'期末','2026-01-10','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(2,1,'MySQL数据库',92.00,'期末','2026-01-12','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(3,1,'数据结构',78.00,'期末','2026-01-15','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(4,2,'Java程序设计',90.00,'期末','2026-01-10','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(5,2,'MySQL数据库',88.50,'期末','2026-01-12','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(6,3,'Java程序设计',76.00,'期末','2026-01-10','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(7,3,'数据结构',82.50,'期末','2026-01-15','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(8,4,'MySQL数据库',95.00,'期末','2026-01-12','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(9,5,'Java程序设计',88.00,'期末','2026-01-10','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42'),(10,6,'数据结构',91.50,'期末','2026-01-15','2025-2026-1','2026-06-27 17:05:42','2026-06-27 17:05:42');
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_no` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` varchar(10) DEFAULT '男' COMMENT '性别：男/女',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `clazz_id` int DEFAULT NULL COMMENT '所属班级ID',
  `status` varchar(20) DEFAULT '在读' COMMENT '状态：在读/休学/毕业/退学',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_no` (`student_no`),
  KEY `clazz_id` (`clazz_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`clazz_id`) REFERENCES `clazz` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'2024001','张三','男','13800138001','zhangsan@example.com',NULL,'在读','2026-06-27 15:08:30','2026-06-27 15:08:30'),(2,'2024002','李四','女','13800138002','lisi@example.com',NULL,'在读','2026-06-27 15:08:30','2026-06-27 15:08:30'),(3,'2024003','王五','男','13800138003','wangwu@example.com',NULL,'在读','2026-06-27 15:08:30','2026-06-27 15:08:30'),(4,'2024004','赵六','男','13800138004','zhaoliu@example.com',NULL,'休学','2026-06-27 15:08:30','2026-06-27 15:08:30'),(5,'2024005','孙七','女','13800138005','sunqi@example.com',NULL,'在读','2026-06-27 15:08:30','2026-06-27 15:08:30'),(6,'2024006','周八','男','13800138006','zhouba@example.com',NULL,'毕业','2026-06-27 15:08:30','2026-06-27 15:08:30');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `teacher_no` varchar(20) NOT NULL COMMENT '工号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` varchar(10) DEFAULT '男' COMMENT '性别：男/女',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `title` varchar(50) DEFAULT NULL COMMENT '职称：教授/副教授/讲师/助教',
  `department` varchar(100) DEFAULT NULL COMMENT '所属院系',
  `hire_date` date DEFAULT NULL COMMENT '入职日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `teacher_no` (`teacher_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教师表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'T2024001','张教授','男','13800139001','zhang@example.com','教授','计算机科学学院','2010-09-01','2026-06-27 17:58:17','2026-06-27 17:58:17'),(2,'T2024002','李副教授','女','13800139002','li@example.com','副教授','计算机科学学院','2015-09-01','2026-06-27 17:58:17','2026-06-27 17:58:17'),(3,'T2024003','王讲师','男','13800139003','wang@example.com','讲师','软件工程学院','2018-09-01','2026-06-27 17:58:17','2026-06-27 17:58:17'),(4,'T2024004','赵讲师','女','13800139004','zhao@example.com','讲师','计算机科学学院','2020-09-01','2026-06-27 17:58:17','2026-06-27 17:58:17'),(5,'T2024005','孙助教','男','13800139005','sun@example.com','助教','软件工程学院','2022-09-01','2026-06-27 17:58:17','2026-06-27 17:58:17');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'user',
  `email` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (5,'111','3naXXG6mIcqxZ3e3guEbmw==$QtBqQ8whDVDDl/jnBFAsepVgnLvjhnW4WmWMvva4jUk=','admin','','2026-06-27 06:46:11','2026-07-18 07:58:00'),(6,'222','X678Y9y3oyXwUMzGJFey+g==$N3CYqXRxz1oOLX6PR74ltivBBbVVwQ8EU8tiXjnnWP4=','user','','2026-06-27 06:52:54','2026-07-01 00:38:21'),(7,'jym','INcnHY8XpeLMOZG1UPwlAw==$W6qKx1651MEwZFgWrEuX3tZwVXmGSPnzWHVvqaHgRJE=','admin','','2026-07-01 01:14:42','2026-07-01 01:19:34'),(8,'jym2','+DUldpdVoCIEX5PNZzprow==$y7kMhIIG56KIomuNctHl4jMtDWdtgIGZ27zjvLpCCak=','user','','2026-07-01 01:14:57','2026-07-01 01:20:11');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'clazz_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-18 19:43:18
