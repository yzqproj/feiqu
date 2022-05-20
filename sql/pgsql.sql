/*
 Navicat Premium Data Transfer

 Source Server         : localpg
 Source Server Type    : PostgreSQL
 Source Server Version : 140002
 Source Host           : localhost:5432
 Source Catalog        : feiqu
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140002
 File Encoding         : 65001

 Date: 20/05/2022 13:09:31
*/


-- ----------------------------
-- Table structure for api_doc_interface
-- ----------------------------
DROP TABLE IF EXISTS "public"."api_doc_interface";
CREATE TABLE "public"."api_doc_interface" (
  "ID" int8 NOT NULL,
  "url" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "param" text COLLATE "pg_catalog"."default",
  "paramRemark" text COLLATE "pg_catalog"."default",
  "requestExam" text COLLATE "pg_catalog"."default",
  "responseParam" text COLLATE "pg_catalog"."default",
  "errorList" text COLLATE "pg_catalog"."default",
  "trueExam" text COLLATE "pg_catalog"."default",
  "falseExam" text COLLATE "pg_catalog"."default",
  "status" int2 NOT NULL,
  "moduleId" int8 NOT NULL,
  "interfaceName" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" text COLLATE "pg_catalog"."default",
  "errors" text COLLATE "pg_catalog"."default",
  "updateBy" varchar(100) COLLATE "pg_catalog"."default",
  "updateTime" timestamp(6) NOT NULL,
  "createTime" timestamp(6) NOT NULL,
  "version" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "sequence" int4 NOT NULL,
  "header" text COLLATE "pg_catalog"."default",
  "fullUrl" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "monitorEmails" varchar(200) COLLATE "pg_catalog"."default",
  "isTemplate" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "projectId" int8 NOT NULL,
  "contentType" varchar(45) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."api_doc_interface"."url" IS 'api链接';
COMMENT ON COLUMN "public"."api_doc_interface"."method" IS ' 请求方式';
COMMENT ON COLUMN "public"."api_doc_interface"."param" IS '参数列表';
COMMENT ON COLUMN "public"."api_doc_interface"."paramRemark" IS '请求参数备注';
COMMENT ON COLUMN "public"."api_doc_interface"."requestExam" IS '请求示例';
COMMENT ON COLUMN "public"."api_doc_interface"."responseParam" IS '返回参数说明';
COMMENT ON COLUMN "public"."api_doc_interface"."errorList" IS '接口错误码列表';
COMMENT ON COLUMN "public"."api_doc_interface"."trueExam" IS '正确返回示例';
COMMENT ON COLUMN "public"."api_doc_interface"."falseExam" IS '错误返回示例';
COMMENT ON COLUMN "public"."api_doc_interface"."status" IS '是否可用;0不可用；1可用;-1 删除';
COMMENT ON COLUMN "public"."api_doc_interface"."moduleId" IS '所属模块ID';
COMMENT ON COLUMN "public"."api_doc_interface"."interfaceName" IS '接口名';
COMMENT ON COLUMN "public"."api_doc_interface"."errors" IS '错误码、错误码信息';
COMMENT ON COLUMN "public"."api_doc_interface"."version" IS '版本号';
COMMENT ON COLUMN "public"."api_doc_interface"."sequence" IS '排序，越大越靠前';
COMMENT ON COLUMN "public"."api_doc_interface"."isTemplate" IS '是否是模板';
COMMENT ON COLUMN "public"."api_doc_interface"."contentType" IS '接口返回contentType';
COMMENT ON TABLE "public"."api_doc_interface" IS 'api文档接口';

-- ----------------------------
-- Records of api_doc_interface
-- ----------------------------

-- ----------------------------
-- Table structure for api_doc_module
-- ----------------------------
DROP TABLE IF EXISTS "public"."api_doc_module";
CREATE TABLE "public"."api_doc_module" (
  "ID" int8 NOT NULL,
  "MODULE_NAME" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "CREATE_TIME" timestamp(6) NOT NULL,
  "STATUS" int2 NOT NULL,
  "URL" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "REMARK" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "USER_ID" int4 NOT NULL,
  "PROJECT_ID" int8 NOT NULL
)
;
COMMENT ON TABLE "public"."api_doc_module" IS '模块';

-- ----------------------------
-- Records of api_doc_module
-- ----------------------------

-- ----------------------------
-- Table structure for api_doc_project
-- ----------------------------
DROP TABLE IF EXISTS "public"."api_doc_project";
CREATE TABLE "public"."api_doc_project" (
  "ID" int8 NOT NULL,
  "USER_ID" int4 NOT NULL,
  "PROJECT_NAME" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "CREATE_TIME" timestamp(6) NOT NULL,
  "STATUS" int2 NOT NULL,
  "REMARK" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "TYPE" int2 NOT NULL,
  "PASSWORD" varchar(45) COLLATE "pg_catalog"."default" NOT NULL,
  "COVER" varchar(200) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."api_doc_project"."USER_ID" IS '创建人的id';
COMMENT ON COLUMN "public"."api_doc_project"."PROJECT_NAME" IS '项目名称';
COMMENT ON TABLE "public"."api_doc_project" IS 'api文档项目';

-- ----------------------------
-- Records of api_doc_project
-- ----------------------------

-- ----------------------------
-- Table structure for api_doc_project_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."api_doc_project_user";
CREATE TABLE "public"."api_doc_project_user" (
  "ID" int8 NOT NULL,
  "PROJECT_ID" int8 NOT NULL,
  "USER_ID" int4 NOT NULL,
  "CREATE_TIME" timestamp(6) NOT NULL,
  "STATUS" int4 NOT NULL,
  "sponsor" int4 NOT NULL
)
;
COMMENT ON TABLE "public"."api_doc_project_user" IS '项目与用户的关联表';

-- ----------------------------
-- Records of api_doc_project_user
-- ----------------------------

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS "public"."article";
CREATE TABLE "public"."article" (
  "id" int4 NOT NULL,
  "article_title" varchar(255) COLLATE "pg_catalog"."default",
  "article_content" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "user_id" int4,
  "del_flag" int4,
  "like_count" int4,
  "comment_count" int4,
  "browse_count" int4,
  "label" int4,
  "anonymous_switch" int4 NOT NULL,
  "is_recommend" int4,
  "content_type" int4 NOT NULL
)
;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for c_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."c_message";
CREATE TABLE "public"."c_message" (
  "id" int4 NOT NULL,
  "content" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "post_user_id" int4,
  "received_user_id" int4,
  "del_flag" int4,
  "type" int4,
  "is_read" int4
)
;

-- ----------------------------
-- Records of c_message
-- ----------------------------

-- ----------------------------
-- Table structure for fq_advertisement
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_advertisement";
CREATE TABLE "public"."fq_advertisement" (
  "ID" int4 NOT NULL,
  "PIC_URL" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "REMARK" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "AD_HREF" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "POSITION" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_advertisement"."PIC_URL" IS '广告图片';
COMMENT ON COLUMN "public"."fq_advertisement"."REMARK" IS '备注';
COMMENT ON COLUMN "public"."fq_advertisement"."GMT_CREATE" IS '创建时间';
COMMENT ON COLUMN "public"."fq_advertisement"."AD_HREF" IS '广告跳转链接';
COMMENT ON COLUMN "public"."fq_advertisement"."POSITION" IS '0 首页banner 1 列表页 2 详情页';
COMMENT ON TABLE "public"."fq_advertisement" IS '广告配置';

-- ----------------------------
-- Records of fq_advertisement
-- ----------------------------

-- ----------------------------
-- Table structure for fq_area
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_area";
CREATE TABLE "public"."fq_area" (
  "id" int4 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "card_num" int4
)
;

-- ----------------------------
-- Records of fq_area
-- ----------------------------

-- ----------------------------
-- Table structure for fq_background_img
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_background_img";
CREATE TABLE "public"."fq_background_img" (
  "id" int4 NOT NULL,
  "img_url" varchar(255) COLLATE "pg_catalog"."default",
  "del_flag" int4,
  "user_id" int4,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "history_urls" varchar(500) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of fq_background_img
-- ----------------------------

-- ----------------------------
-- Table structure for fq_black_list
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_black_list";
CREATE TABLE "public"."fq_black_list" (
  "ID" int8 NOT NULL,
  "IP" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "OPERATOR" varchar(45) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_black_list"."IP" IS '被拉黑的ip';
COMMENT ON TABLE "public"."fq_black_list" IS '黑名单';

-- ----------------------------
-- Records of fq_black_list
-- ----------------------------

-- ----------------------------
-- Table structure for fq_change_log_collect
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_change_log_collect";
CREATE TABLE "public"."fq_change_log_collect" (
  "id" int4 NOT NULL,
  "title" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "watch_count" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_change_log_collect"."title" IS '标题';
COMMENT ON COLUMN "public"."fq_change_log_collect"."watch_count" IS '观看数量';
COMMENT ON TABLE "public"."fq_change_log_collect" IS '更新日志收集';

-- ----------------------------
-- Records of fq_change_log_collect
-- ----------------------------

-- ----------------------------
-- Table structure for fq_collect
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_collect";
CREATE TABLE "public"."fq_collect" (
  "id" int4 NOT NULL,
  "topic_type" int4,
  "topic_id" int4,
  "del_flag" int4,
  "create_time" timestamp(6),
  "user_id" int4
)
;
COMMENT ON TABLE "public"."fq_collect" IS '收藏表';

-- ----------------------------
-- Records of fq_collect
-- ----------------------------

-- ----------------------------
-- Table structure for fq_doutu_cloud
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_doutu_cloud";
CREATE TABLE "public"."fq_doutu_cloud" (
  "ID" int8 NOT NULL,
  "USER_ID" int4 NOT NULL,
  "IMG_URL" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "DEL_FLAG" int4 NOT NULL,
  "TAG" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "VIDEO_URL" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "TITLE" varchar(100) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_doutu_cloud"."TAG" IS '标签 逗号隔开';
COMMENT ON COLUMN "public"."fq_doutu_cloud"."TITLE" IS '标题';
COMMENT ON TABLE "public"."fq_doutu_cloud" IS '斗图云';

-- ----------------------------
-- Records of fq_doutu_cloud
-- ----------------------------

-- ----------------------------
-- Table structure for fq_friend_link
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_friend_link";
CREATE TABLE "public"."fq_friend_link" (
  "id" int4 NOT NULL,
  "link_name" varchar(255) COLLATE "pg_catalog"."default",
  "link_url" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;

-- ----------------------------
-- Records of fq_friend_link
-- ----------------------------

-- ----------------------------
-- Table structure for fq_good_pic
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_good_pic";
CREATE TABLE "public"."fq_good_pic" (
  "ID" int8 NOT NULL,
  "TITLE" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "PIC_URL_LIST" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_good_pic"."PIC_URL_LIST" IS '图片列表';
COMMENT ON COLUMN "public"."fq_good_pic"."GMT_CREATE" IS '创建时间';
COMMENT ON TABLE "public"."fq_good_pic" IS '好资源的图片';

-- ----------------------------
-- Records of fq_good_pic
-- ----------------------------

-- ----------------------------
-- Table structure for fq_label
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_label";
CREATE TABLE "public"."fq_label" (
  "id" int4 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "del_flag" int4,
  "type" int4
)
;

-- ----------------------------
-- Records of fq_label
-- ----------------------------

-- ----------------------------
-- Table structure for fq_lolita
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_lolita";
CREATE TABLE "public"."fq_lolita" (
  "ID" int8 NOT NULL,
  "PIC_URL" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "USER_ID" int4 NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "DEL_FLAG" int4 NOT NULL,
  "REMARK" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "LINK" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "LIKE_COUNT" int4 NOT NULL,
  "COMMENT_COUNT" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_lolita"."GMT_CREATE" IS '创建时间';
COMMENT ON COLUMN "public"."fq_lolita"."REMARK" IS '备注';
COMMENT ON COLUMN "public"."fq_lolita"."LINK" IS '自动生成的链接 用于分享';
COMMENT ON TABLE "public"."fq_lolita" IS 'LOLITA FASHION洋装';

-- ----------------------------
-- Records of fq_lolita
-- ----------------------------

-- ----------------------------
-- Table structure for fq_music
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_music";
CREATE TABLE "public"."fq_music" (
  "id" int4 NOT NULL,
  "music_name" varchar(255) COLLATE "pg_catalog"."default",
  "music_url" varchar(255) COLLATE "pg_catalog"."default",
  "del_flag" int4,
  "create_time" timestamp(6),
  "like_count" int4,
  "play_count" int4,
  "user_id" int4,
  "lyric" varchar(1000) COLLATE "pg_catalog"."default",
  "singer" varchar(50) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of fq_music
-- ----------------------------

-- ----------------------------
-- Table structure for fq_news
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_news";
CREATE TABLE "public"."fq_news" (
  "ID" int8 NOT NULL,
  "TITLE" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "CONTENT" text COLLATE "pg_catalog"."default",
  "SOURCE" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "COMMENT_COUNT" int4 NOT NULL,
  "IMG_SRC" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "P_TIME" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_news"."ID" IS '主键';
COMMENT ON COLUMN "public"."fq_news"."TITLE" IS '标题';
COMMENT ON TABLE "public"."fq_news" IS '新闻';

-- ----------------------------
-- Records of fq_news
-- ----------------------------

-- ----------------------------
-- Table structure for fq_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_notice";
CREATE TABLE "public"."fq_notice" (
  "id" int4 NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "fq_order" int4,
  "is_show" int4,
  "user_id" int4,
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(50) COLLATE "pg_catalog"."default",
  "comment_num" int4
)
;
COMMENT ON TABLE "public"."fq_notice" IS '通知表';

-- ----------------------------
-- Records of fq_notice
-- ----------------------------

-- ----------------------------
-- Table structure for fq_search_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_search_record";
CREATE TABLE "public"."fq_search_record" (
  "ID" int8 NOT NULL,
  "NAME" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "DEL_FLAG" int4 NOT NULL,
  "TYPE" int4 NOT NULL,
  "USER_ID" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_search_record"."NAME" IS '搜索名称';
COMMENT ON COLUMN "public"."fq_search_record"."TYPE" IS '搜索类别';
COMMENT ON COLUMN "public"."fq_search_record"."USER_ID" IS '用户id';
COMMENT ON TABLE "public"."fq_search_record" IS '搜索记录';

-- ----------------------------
-- Records of fq_search_record
-- ----------------------------

-- ----------------------------
-- Table structure for fq_short_video
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_short_video";
CREATE TABLE "public"."fq_short_video" (
  "id" int8 NOT NULL,
  "url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int4 NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "del_flag" int4 NOT NULL,
  "like_count" int4 NOT NULL,
  "title" varchar(40) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of fq_short_video
-- ----------------------------

-- ----------------------------
-- Table structure for fq_sign
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_sign";
CREATE TABLE "public"."fq_sign" (
  "id" int4 NOT NULL,
  "days" int4 NOT NULL,
  "user_id" int4,
  "sign_time" timestamp(6),
  "sign_days" varchar(255) COLLATE "pg_catalog"."default",
  "max_days" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_sign"."days" IS '实时的连续的签到天数';
COMMENT ON COLUMN "public"."fq_sign"."max_days" IS '签到最长的天数，用于补签';
COMMENT ON TABLE "public"."fq_sign" IS '签到';

-- ----------------------------
-- Records of fq_sign
-- ----------------------------

-- ----------------------------
-- Table structure for fq_third_party
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_third_party";
CREATE TABLE "public"."fq_third_party" (
  "id" int4 NOT NULL,
  "openid" varchar(255) COLLATE "pg_catalog"."default",
  "provider" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" int4,
  "create_time" timestamp(6)
)
;
COMMENT ON TABLE "public"."fq_third_party" IS '第三方';

-- ----------------------------
-- Records of fq_third_party
-- ----------------------------

-- ----------------------------
-- Table structure for fq_topic
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_topic";
CREATE TABLE "public"."fq_topic" (
  "ID" int8 NOT NULL,
  "TITLE" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "CONTENT" text COLLATE "pg_catalog"."default",
  "SOURCE" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "AUTHOR" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "AUTHOR_ICON" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "COMMENT_COUNT" int4 NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "TYPE" varchar(20) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_topic"."ID" IS '主键';
COMMENT ON COLUMN "public"."fq_topic"."TITLE" IS '标题';
COMMENT ON COLUMN "public"."fq_topic"."AUTHOR" IS '作者';
COMMENT ON COLUMN "public"."fq_topic"."AUTHOR_ICON" IS '作者头像';
COMMENT ON TABLE "public"."fq_topic" IS '话题';

-- ----------------------------
-- Records of fq_topic
-- ----------------------------

-- ----------------------------
-- Table structure for fq_topic_reply
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_topic_reply";
CREATE TABLE "public"."fq_topic_reply" (
  "ID" int8 NOT NULL,
  "CONTENT" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "TOPIC_ID" int8 NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL
)
;

-- ----------------------------
-- Records of fq_topic_reply
-- ----------------------------

-- ----------------------------
-- Table structure for fq_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_user";
CREATE TABLE "public"."fq_user" (
  "id" int4 NOT NULL,
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "create_ip" varchar(255) COLLATE "pg_catalog"."default",
  "city" varchar(50) COLLATE "pg_catalog"."default",
  "sex" int4,
  "is_single" int4,
  "is_mail_bind" int4,
  "sign" varchar(255) COLLATE "pg_catalog"."default",
  "openid" varchar(255) COLLATE "pg_catalog"."default",
  "provider" varchar(255) COLLATE "pg_catalog"."default",
  "qudou_num" int4,
  "birth" varchar(255) COLLATE "pg_catalog"."default",
  "education" varchar(255) COLLATE "pg_catalog"."default",
  "school" varchar(255) COLLATE "pg_catalog"."default",
  "role" int4,
  "level" int4,
  "status" int4
)
;

-- ----------------------------
-- Records of fq_user
-- ----------------------------

-- ----------------------------
-- Table structure for fq_user_active_num
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_user_active_num";
CREATE TABLE "public"."fq_user_active_num" (
  "ID" int8 NOT NULL,
  "ACTIVE_NUM" int4 NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "USER_ID" int4 NOT NULL,
  "MARK" varchar(20) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_user_active_num"."ACTIVE_NUM" IS '活跃度';
COMMENT ON COLUMN "public"."fq_user_active_num"."USER_ID" IS '用户id';
COMMENT ON COLUMN "public"."fq_user_active_num"."MARK" IS '标识';
COMMENT ON TABLE "public"."fq_user_active_num" IS '用户活跃度';

-- ----------------------------
-- Records of fq_user_active_num
-- ----------------------------

-- ----------------------------
-- Table structure for fq_user_activity_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_user_activity_record";
CREATE TABLE "public"."fq_user_activity_record" (
  "ID" int8 NOT NULL,
  "USER_ID" int4 NOT NULL,
  "CREATE_TIME" timestamp(6) NOT NULL,
  "DEL_FLAG" int4 NOT NULL,
  "ACTIVITY_CONTENT" varchar(500) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON TABLE "public"."fq_user_activity_record" IS '用户活动记录';

-- ----------------------------
-- Records of fq_user_activity_record
-- ----------------------------

-- ----------------------------
-- Table structure for fq_user_auth
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_user_auth";
CREATE TABLE "public"."fq_user_auth" (
  "ID" int4 NOT NULL,
  "USER_ID" int4 NOT NULL,
  "AUTHED_USER_ID" int4 NOT NULL,
  "AUTH_TIME" timestamp(6) NOT NULL,
  "DEL_FLAG" int4 NOT NULL,
  "AUTH_TYPE" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_user_auth"."USER_ID" IS '授权的用户';
COMMENT ON COLUMN "public"."fq_user_auth"."AUTHED_USER_ID" IS '被授权的用户';
COMMENT ON COLUMN "public"."fq_user_auth"."AUTH_TIME" IS '被授权时间';
COMMENT ON COLUMN "public"."fq_user_auth"."DEL_FLAG" IS '删除标志';
COMMENT ON COLUMN "public"."fq_user_auth"."AUTH_TYPE" IS '授权类型';
COMMENT ON TABLE "public"."fq_user_auth" IS '用户授权';

-- ----------------------------
-- Records of fq_user_auth
-- ----------------------------

-- ----------------------------
-- Table structure for fq_user_pay_way
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_user_pay_way";
CREATE TABLE "public"."fq_user_pay_way" (
  "ID" int8 NOT NULL,
  "PAY_WAY" int4 NOT NULL,
  "PAY_IMG_URL" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "GMT_CREATE" timestamp(6) NOT NULL,
  "USER_ID" int4 NOT NULL,
  "DEL_FLAG" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."fq_user_pay_way"."PAY_WAY" IS '1 支付宝 2 微信';
COMMENT ON COLUMN "public"."fq_user_pay_way"."PAY_IMG_URL" IS '支付的二维码照片';
COMMENT ON COLUMN "public"."fq_user_pay_way"."GMT_CREATE" IS '创建时间';
COMMENT ON COLUMN "public"."fq_user_pay_way"."USER_ID" IS '用户id';
COMMENT ON COLUMN "public"."fq_user_pay_way"."DEL_FLAG" IS '是否删除';
COMMENT ON TABLE "public"."fq_user_pay_way" IS '支付方式';

-- ----------------------------
-- Records of fq_user_pay_way
-- ----------------------------

-- ----------------------------
-- Table structure for fq_visit_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_visit_record";
CREATE TABLE "public"."fq_visit_record" (
  "id" int4 NOT NULL,
  "visit_user_id" int4,
  "visited_user_id" int4,
  "visit_time" timestamp(6),
  "del_flag" int4
)
;

-- ----------------------------
-- Records of fq_visit_record
-- ----------------------------

-- ----------------------------
-- Table structure for fq_website_dir
-- ----------------------------
DROP TABLE IF EXISTS "public"."fq_website_dir";
CREATE TABLE "public"."fq_website_dir" (
  "id" int4 NOT NULL,
  "url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(40) COLLATE "pg_catalog"."default",
  "del_flag" int4,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "click_count" int4,
  "user_id" int4,
  "create_time" timestamp(6),
  "icon" varchar(100) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of fq_website_dir
-- ----------------------------

-- ----------------------------
-- Table structure for general_comment
-- ----------------------------
DROP TABLE IF EXISTS "public"."general_comment";
CREATE TABLE "public"."general_comment" (
  "id" int4 NOT NULL,
  "topic_id" int4,
  "topic_type" int4,
  "content" varchar(255) COLLATE "pg_catalog"."default",
  "post_user_id" int4,
  "create_time" timestamp(6),
  "like_count" int4,
  "del_flag" int4,
  "has_reply" int4
)
;
COMMENT ON TABLE "public"."general_comment" IS '评论表设计';

-- ----------------------------
-- Records of general_comment
-- ----------------------------

-- ----------------------------
-- Table structure for general_like
-- ----------------------------
DROP TABLE IF EXISTS "public"."general_like";
CREATE TABLE "public"."general_like" (
  "id" int4 NOT NULL,
  "topic_id" int4,
  "topic_type" int4,
  "like_value" int4,
  "post_user_id" int4,
  "create_time" timestamp(6),
  "del_flag" int4
)
;
COMMENT ON TABLE "public"."general_like" IS '点赞表';

-- ----------------------------
-- Records of general_like
-- ----------------------------

-- ----------------------------
-- Table structure for general_reply
-- ----------------------------
DROP TABLE IF EXISTS "public"."general_reply";
CREATE TABLE "public"."general_reply" (
  "id" int4 NOT NULL,
  "comment_id" int4,
  "content" varchar(255) COLLATE "pg_catalog"."default",
  "post_user_id" int4,
  "to_user_id" int4,
  "type" int4,
  "create_time" timestamp(6),
  "del_flag" int4,
  "reply_id" int4
)
;
COMMENT ON TABLE "public"."general_reply" IS '回复评论的';

-- ----------------------------
-- Records of general_reply
-- ----------------------------

-- ----------------------------
-- Table structure for job_talk
-- ----------------------------
DROP TABLE IF EXISTS "public"."job_talk";
CREATE TABLE "public"."job_talk" (
  "id" int4 NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "user_id" int4,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "del_flag" int4,
  "comment_count" int4,
  "label" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4,
  "last_pub_nickname" varchar(255) COLLATE "pg_catalog"."default",
  "last_pub_time" timestamp(6),
  "see_count" int4
)
;

-- ----------------------------
-- Records of job_talk
-- ----------------------------

-- ----------------------------
-- Table structure for nginx_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."nginx_log";
CREATE TABLE "public"."nginx_log" (
  "id" int4 NOT NULL,
  "ip" varchar(255) COLLATE "pg_catalog"."default",
  "local_time" varchar(255) COLLATE "pg_catalog"."default",
  "method" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "http" varchar(255) COLLATE "pg_catalog"."default",
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "bytes" varchar(255) COLLATE "pg_catalog"."default",
  "referer" varchar(255) COLLATE "pg_catalog"."default",
  "xforward" varchar(255) COLLATE "pg_catalog"."default",
  "request_time" float8,
  "user_agent" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "spider_type" int4
)
;
COMMENT ON COLUMN "public"."nginx_log"."spider_type" IS '//0代表没有爬虫 1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗';

-- ----------------------------
-- Records of nginx_log
-- ----------------------------

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS "public"."question";
CREATE TABLE "public"."question" (
  "id" int4 NOT NULL,
  "que_content" varchar(255) COLLATE "pg_catalog"."default",
  "createtime" timestamp(6),
  "del_flag" int4,
  "user_id" int4
)
;

-- ----------------------------
-- Records of question
-- ----------------------------

-- ----------------------------
-- Table structure for super_beauty
-- ----------------------------
DROP TABLE IF EXISTS "public"."super_beauty";
CREATE TABLE "public"."super_beauty" (
  "id" int4 NOT NULL,
  "img_url" varchar(255) COLLATE "pg_catalog"."default",
  "upload_user_id" int4,
  "create_time" timestamp(6),
  "del_flag" int4,
  "like_count" int4,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "category" varchar(255) COLLATE "pg_catalog"."default",
  "pic_list" varchar(500) COLLATE "pg_catalog"."default",
  "pic_desc_list" varchar(500) COLLATE "pg_catalog"."default",
  "see_count" int4
)
;
COMMENT ON COLUMN "public"."super_beauty"."category" IS '类别';

-- ----------------------------
-- Records of super_beauty
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int4 NOT NULL,
  "job_name" varchar(64) COLLATE "pg_catalog"."default",
  "job_group" varchar(64) COLLATE "pg_catalog"."default",
  "method_name" varchar(500) COLLATE "pg_catalog"."default",
  "method_params" varchar(50) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
  "misfire_policy" varchar(20) COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default",
  "create_by" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "concurrent" char(1) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS '任务ID';
COMMENT ON COLUMN "public"."sys_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job"."method_name" IS '任务方法';
COMMENT ON COLUMN "public"."sys_job"."method_params" IS '方法参数';
COMMENT ON COLUMN "public"."sys_job"."cron_expression" IS 'cron执行表达式';
COMMENT ON COLUMN "public"."sys_job"."misfire_policy" IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
COMMENT ON COLUMN "public"."sys_job"."status" IS '状态（0正常 1暂停）';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_job"."remark" IS '备注信息';
COMMENT ON COLUMN "public"."sys_job"."concurrent" IS '是否并发执行（0允许 1禁止）';
COMMENT ON TABLE "public"."sys_job" IS '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log" (
  "job_log_id" int4 NOT NULL,
  "job_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "method_name" varchar(500) COLLATE "pg_catalog"."default",
  "method_params" varchar(50) COLLATE "pg_catalog"."default",
  "job_message" varchar(500) COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default",
  "exception_info" varchar(2000) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_job_log"."job_log_id" IS '任务日志ID';
COMMENT ON COLUMN "public"."sys_job_log"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job_log"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job_log"."method_name" IS '任务方法';
COMMENT ON COLUMN "public"."sys_job_log"."method_params" IS '方法参数';
COMMENT ON COLUMN "public"."sys_job_log"."job_message" IS '日志信息';
COMMENT ON COLUMN "public"."sys_job_log"."status" IS '执行状态（0正常 1失败）';
COMMENT ON COLUMN "public"."sys_job_log"."exception_info" IS '异常信息';
COMMENT ON COLUMN "public"."sys_job_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_job_log" IS '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for thought
-- ----------------------------
DROP TABLE IF EXISTS "public"."thought";
CREATE TABLE "public"."thought" (
  "id" int4 NOT NULL,
  "thought_content" varchar(400) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "user_id" int4,
  "like_count" int4,
  "comment_count" int4,
  "del_flag" int4,
  "area" varchar(100) COLLATE "pg_catalog"."default",
  "last_reply_time" varchar(50) COLLATE "pg_catalog"."default",
  "pic_list" varchar(500) COLLATE "pg_catalog"."default",
  "last_reply_user_name" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "public"."thought" IS '想法';

-- ----------------------------
-- Records of thought
-- ----------------------------

-- ----------------------------
-- Table structure for upload_img_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."upload_img_record";
CREATE TABLE "public"."upload_img_record" (
  "id" int4 NOT NULL,
  "pic_url" varchar(255) COLLATE "pg_catalog"."default",
  "pic_md5" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "ip" varchar(100) COLLATE "pg_catalog"."default",
  "user_id" int4,
  "pic_size" int8
)
;

-- ----------------------------
-- Records of upload_img_record
-- ----------------------------

-- ----------------------------
-- Table structure for user_activate
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_activate";
CREATE TABLE "public"."user_activate" (
  "id" int4 NOT NULL,
  "user_id" int4,
  "token" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;

-- ----------------------------
-- Records of user_activate
-- ----------------------------

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_follow";
CREATE TABLE "public"."user_follow" (
  "id" int4 NOT NULL,
  "follower_user_id" int4,
  "followed_user_id" int4,
  "create_time" timestamp(6),
  "del_flag" int4
)
;
COMMENT ON TABLE "public"."user_follow" IS '用户关注表';

-- ----------------------------
-- Records of user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for user_time_line
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_time_line";
CREATE TABLE "public"."user_time_line" (
  "id" int4 NOT NULL,
  "content" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "del_flag" int4,
  "user_id" int4
)
;

-- ----------------------------
-- Records of user_time_line
-- ----------------------------

-- ----------------------------
-- Table structure for wang_hong_wan
-- ----------------------------
DROP TABLE IF EXISTS "public"."wang_hong_wan";
CREATE TABLE "public"."wang_hong_wan" (
  "ID" int8 NOT NULL,
  "AUTHOR" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "AREA" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "CONTENT" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "PIC_LIST" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON TABLE "public"."wang_hong_wan" IS '网红玩';

-- ----------------------------
-- Records of wang_hong_wan
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table api_doc_interface
-- ----------------------------
ALTER TABLE "public"."api_doc_interface" ADD CONSTRAINT "api_doc_interface_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table api_doc_module
-- ----------------------------
ALTER TABLE "public"."api_doc_module" ADD CONSTRAINT "api_doc_module_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table api_doc_project
-- ----------------------------
ALTER TABLE "public"."api_doc_project" ADD CONSTRAINT "api_doc_project_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table api_doc_project_user
-- ----------------------------
ALTER TABLE "public"."api_doc_project_user" ADD CONSTRAINT "api_doc_project_user_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table article
-- ----------------------------
ALTER TABLE "public"."article" ADD CONSTRAINT "article_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table c_message
-- ----------------------------
ALTER TABLE "public"."c_message" ADD CONSTRAINT "c_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_advertisement
-- ----------------------------
ALTER TABLE "public"."fq_advertisement" ADD CONSTRAINT "fq_advertisement_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_area
-- ----------------------------
ALTER TABLE "public"."fq_area" ADD CONSTRAINT "fq_area_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_background_img
-- ----------------------------
ALTER TABLE "public"."fq_background_img" ADD CONSTRAINT "fq_background_img_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_black_list
-- ----------------------------
ALTER TABLE "public"."fq_black_list" ADD CONSTRAINT "fq_black_list_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_change_log_collect
-- ----------------------------
ALTER TABLE "public"."fq_change_log_collect" ADD CONSTRAINT "fq_change_log_collect_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_collect
-- ----------------------------
ALTER TABLE "public"."fq_collect" ADD CONSTRAINT "fq_collect_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_doutu_cloud
-- ----------------------------
ALTER TABLE "public"."fq_doutu_cloud" ADD CONSTRAINT "fq_doutu_cloud_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_friend_link
-- ----------------------------
ALTER TABLE "public"."fq_friend_link" ADD CONSTRAINT "fq_friend_link_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_good_pic
-- ----------------------------
ALTER TABLE "public"."fq_good_pic" ADD CONSTRAINT "fq_good_pic_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_label
-- ----------------------------
ALTER TABLE "public"."fq_label" ADD CONSTRAINT "fq_label_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_lolita
-- ----------------------------
ALTER TABLE "public"."fq_lolita" ADD CONSTRAINT "fq_lolita_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_music
-- ----------------------------
ALTER TABLE "public"."fq_music" ADD CONSTRAINT "fq_music_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_news
-- ----------------------------
ALTER TABLE "public"."fq_news" ADD CONSTRAINT "fq_news_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_notice
-- ----------------------------
ALTER TABLE "public"."fq_notice" ADD CONSTRAINT "fq_notice_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_search_record
-- ----------------------------
ALTER TABLE "public"."fq_search_record" ADD CONSTRAINT "fq_search_record_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_short_video
-- ----------------------------
ALTER TABLE "public"."fq_short_video" ADD CONSTRAINT "fq_short_video_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_sign
-- ----------------------------
ALTER TABLE "public"."fq_sign" ADD CONSTRAINT "fq_sign_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_third_party
-- ----------------------------
ALTER TABLE "public"."fq_third_party" ADD CONSTRAINT "fq_third_party_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_topic
-- ----------------------------
ALTER TABLE "public"."fq_topic" ADD CONSTRAINT "fq_topic_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_topic_reply
-- ----------------------------
ALTER TABLE "public"."fq_topic_reply" ADD CONSTRAINT "fq_topic_reply_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_user
-- ----------------------------
ALTER TABLE "public"."fq_user" ADD CONSTRAINT "fq_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_user_active_num
-- ----------------------------
ALTER TABLE "public"."fq_user_active_num" ADD CONSTRAINT "fq_user_active_num_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_user_activity_record
-- ----------------------------
ALTER TABLE "public"."fq_user_activity_record" ADD CONSTRAINT "fq_user_activity_record_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_user_auth
-- ----------------------------
ALTER TABLE "public"."fq_user_auth" ADD CONSTRAINT "fq_user_auth_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_user_pay_way
-- ----------------------------
ALTER TABLE "public"."fq_user_pay_way" ADD CONSTRAINT "fq_user_pay_way_pkey" PRIMARY KEY ("ID");

-- ----------------------------
-- Primary Key structure for table fq_visit_record
-- ----------------------------
ALTER TABLE "public"."fq_visit_record" ADD CONSTRAINT "fq_visit_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table fq_website_dir
-- ----------------------------
ALTER TABLE "public"."fq_website_dir" ADD CONSTRAINT "fq_website_dir_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table general_comment
-- ----------------------------
ALTER TABLE "public"."general_comment" ADD CONSTRAINT "general_comment_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table general_like
-- ----------------------------
ALTER TABLE "public"."general_like" ADD CONSTRAINT "general_like_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table general_reply
-- ----------------------------
ALTER TABLE "public"."general_reply" ADD CONSTRAINT "general_reply_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table job_talk
-- ----------------------------
ALTER TABLE "public"."job_talk" ADD CONSTRAINT "job_talk_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table nginx_log
-- ----------------------------
ALTER TABLE "public"."nginx_log" ADD CONSTRAINT "nginx_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table question
-- ----------------------------
ALTER TABLE "public"."question" ADD CONSTRAINT "question_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table super_beauty
-- ----------------------------
ALTER TABLE "public"."super_beauty" ADD CONSTRAINT "super_beauty_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log" ADD CONSTRAINT "sys_job_log_pkey" PRIMARY KEY ("job_log_id");

-- ----------------------------
-- Primary Key structure for table thought
-- ----------------------------
ALTER TABLE "public"."thought" ADD CONSTRAINT "thought_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table upload_img_record
-- ----------------------------
ALTER TABLE "public"."upload_img_record" ADD CONSTRAINT "upload_img_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table user_activate
-- ----------------------------
ALTER TABLE "public"."user_activate" ADD CONSTRAINT "user_activate_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table user_follow
-- ----------------------------
ALTER TABLE "public"."user_follow" ADD CONSTRAINT "user_follow_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table user_time_line
-- ----------------------------
ALTER TABLE "public"."user_time_line" ADD CONSTRAINT "user_time_line_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wang_hong_wan
-- ----------------------------
ALTER TABLE "public"."wang_hong_wan" ADD CONSTRAINT "wang_hong_wan_pkey" PRIMARY KEY ("ID");
