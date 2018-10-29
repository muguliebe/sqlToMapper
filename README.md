# sql to mapper
sql file(syntax needed) to mybatis mapper file

## sql file syntax
```sql
/*
{
  "mapperFileName": "common",
  "namespace": "com.zany",
  "queryType": "select",
  "id": "selectAllTables",
  "parameterType": "map",
  "resultType": "map"
}
*/
select a.owner, a.table_name
  from all_tables a
```

## converted mapper file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.zany">
  <select id="selectAllTables" xml:space="preserve" parameterType="map" resultType="map">
    select a.owner, a.table_name
      from all_tables a
  </select>
</mapper>
```

# Installation
```bash
npm install -g sqltomapper
```

