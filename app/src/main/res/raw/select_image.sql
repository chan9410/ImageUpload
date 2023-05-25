SELECT ORIGNL_FILE_NM
  FROM rfid_asset_file
 WHERE IFNULL(ASSET_NO, '') LIKE '%' || ? || '%' COLLATE NOCASE
   AND USE_YN = 'Y' COLLATE NOCASE