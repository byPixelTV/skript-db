> [!IMPORTANT]
> # 📢 ABOUT THE ADDON 🍇:
> - This is not my addon, it is just a fork from https://git.limework.net/Limework/skript-db, the original contributors are btk5h, FranKusmiruk, Govindas and TPGamesNL. I just updated this project, to work with the newest Skript version.

# skript-db

 > Sensible SQL support for Skript.
---

### Difference from original skript-db (by btk5h, FranKusmiruk, Govindas and TPGamesNL)
- Fixed local variables disappearance in newer Skript versions (very hacky fix, but it works, so that's good!)
- Uses newer versions of dependencies (Increased performance and security)
- Replaced `synchronously execute` with `quickly execute`, which allows to speed up queries by 50ms with some risk
- If a sql query is detected to be running on non-main thread, it becomes synchronous automatically
- SQL Driver is configurable both in config and in database connection, comes with shaded MariaDB and PostgreSQL drivers
- A few variable type related bugs fixed
- Uses Java 21 instead of Java 11


### Installation
1. Use 1.21+ Minecraft server version.
2. Use Skript 2.9+
3. Use Java 21+
4. Put skript-db in plugins folder and restart the server
### Expression `Data Source` => `datasource`
Stores the connection information for a data source. This should be saved to a variable in a
 `script load` event or manually through an effect command.

 The url format for your database may vary depending on database you are using. 
 MariaDB/PostgreSQL users: make sure to check `config.yml` to use the correct driver.
#### Syntax
```
[the] data(base|[ ]source) [(of|at)] %string% [with [a] [max[imum]] [connection] life[ ]time of %timespan%] [[(using|with)] [a] driver %-string%]
```

#### Examples
```
set {sql} to the database "mysql://localhost:3306/mydatabase?user=admin&password=12345&useSSL=false"
set {sql} to the database "mariadb://localhost:3306/mydatabase?user=admin&password=12345&useSSL=false"
set {sql} to the database "postgresql://localhost:3306/mydatabase?user=admin&password=12345&ssl=false"
set {sql} to the database "sqlite:database.db"

# Extra parameters:
set {sql} to the database "postgresql://localhost:3306/mydatabase?user=admin&password=12345&ssl=false" with a maximum connection lifetime of 30 minutes
set {sql} to the database "postgresql://localhost:3306/mydatabase?user=admin&password=12345&ssl=false" with a maximum connection lifetime of 30 minutes using driver "org.postgresql.Driver"
```

---

### Effect `Execute Statement`
Executes a statement on a database and optionally stores the result in a variable. Expressions
 embedded in the query will be escaped to avoid SQL injection.

 If a single variable, such as `{test}`, is passed, the variable will be set to the number of
 affected rows.

 If a list variable, such as `{test::*}`, is passed, the query result will be mapped to the list
 variable in the form `{test::<column name>::<row number>}`

 If `quickly` is specified, the SQL query will be done without jumping back to main thread, which speeds it up by 50ms, however that makes code after it to also be on separate thread, you can jump back to main thread by adding `wait a tick`
#### Syntax
```
[quickly] execute %string% (in|on) %datasource% [and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %-objects%]
```

#### Examples
```
execute "select * from table" in {sql} and store the result in {output::*}
```
```
execute "select * from %{table variable}%" in {sql} and store the result in {output::*}
```

---

### Expression `Last Data Source Error` => `text`
Stores the error from the last executed statement, if there was one.
#### Syntax
```
[the] [last] (sql|db|data(base|[ ]source)) error
```

---

### Expression `Unsafe Expression` => `text`
Opts out of automatic SQL injection protection for a specific expression in a statement.
Note: If using PostgreSQL, this will always be needed, due to skript-db not supporting SQL injection protection for PostgreSQL currently.
#### Syntax
```
unsafe %text%
```

#### Examples
```
execute "select %unsafe {columns variable}% from %{table variable}%" in {sql}
```
```
execute unsafe {fully dynamic query} in {sql}
```

#### FAQ: How to return sql data in a function?
You can't because functions don't allow delays, but you can use skript-reflect sections for this:
```
on load:
	create new section stored in {-section::getPlayersFromDatabase}:
		execute "SELECT uuid FROM table" in {-sql} and store the result in {_result::*}
		return {_result::uuid::*}
command /showplayers [<text>]:
	trigger:
		run section {-section::getPlayersFromDatabase} async and store result in {_uuids::*} and wait
		send "%{_uuids::*}%"
```
---
### Configuration
plugins/skript-db/config.yml
```
# Only change this if you wish to use a different driver than Java's default, like MariaDB driver.
# If you use MariaDB, its driver is shaded together with skript-db, so you can just specify: "org.mariadb.jdbc.Driver"
sql-driver-class-name: "default"
```
