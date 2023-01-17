# ParkingSystemManagement

Database related Details:
Database name : parkingsystem
Table-1 : allocation_table (floorid,bayid,status)
PrimaryKey: floorid,bayid

Table-2 : parking_table (parkingid,size,floorid,bayid)
PrimaryKey: parkingid
ForeignKeys: floorid,bayid references to allocation_table (floorid,bayid)
