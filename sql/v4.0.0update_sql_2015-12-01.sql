
alter table user_info add column company_type varchar(8);
alter table company add column company_type varchar(8);

COMMENT ON COLUMN "public"."user_info"."company_type" IS '公司类型';
COMMENT ON COLUMN "public"."company"."company_type" IS '公司类型';


ALTER TABLE "public"."otainfo_company_ref"
ADD COLUMN "create_date" timestamp(6);

ALTER TABLE  "public"."otainfo_company_ref"
ADD COLUMN "tb_type" varchar(16) COLLATE "default";

COMMENT ON COLUMN "public"."otainfo_company_ref"."tb_type" IS 'TB的直连类型（其余的渠道为null）';


DROP INDEX "public"."bang_inn_company_id_inn_id_idx";
CREATE INDEX "bang_inn_company_id_inn_id_idx" ON "public"."bang_inn" USING btree ("company_id" "pg_catalog"."text_ops", "inn_id" "pg_catalog"."int4_ops", "inn_label_id" "pg_catalog"."text_ops", "sj");

DROP INDEX "public"."ota_inn_ota_wg_hid_company_id_bang_inn_id_ota_info_id_idx";
CREATE INDEX "ota_inn_ota_wg_hid_company_id_bang_inn_id_ota_info_id_idx" ON "public"."ota_inn_ota" USING btree ("wg_hid" "pg_catalog"."text_ops", "company_id" "pg_catalog"."text_ops", "bang_inn_id" "pg_catalog"."text_ops", "ota_info_id" "pg_catalog"."text_ops", "sj");

