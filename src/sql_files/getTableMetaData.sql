select COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_DEFAULT
from Information_schema.columns 
where Table_name like ? AND table_schema = ?;