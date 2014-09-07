CREATE TRIGGER [dbo].[TRG_createLogTable_department] ON [dbo].[department]
AFTER INSERT AS
BEGIN
DECLARE @id int
DECLARE @nombrTabla NVARCHAR(128)
SELECT @id = i.id FROM INSERTED i
SELECT @nombrTabla = OBJECT_NAME(parent_object_id)
FROM sys.objects WHERE name = OBJECT_NAME(@@PROCID)
INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)
VALUES(@id,'Inserccion',@nombrTabla,'1')
END
