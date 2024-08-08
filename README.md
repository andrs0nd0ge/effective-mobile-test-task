## The test task of the _Effective Mobile_ company
### 🟦 Description:
The application is a task management system, which operates its own (_internal_) tasks database. The application allows adding tasks to the database and deleting them, as well as fetching all entries from it.

### 🟩 Application Start:
The application uses `PostgreSQL`.\
It can be started by running the ```main``` method of the ```Application``` class located in the `src/main/java/app` folder.\
The application has integrated `Swagger UI`, which can be accessed by opening `localhost:8080/swagger-ui` after the application has started (it is documented).

### ❗ Important Notes:
The app automatically creates the schema and the tables **in default database** for performing the database operations, so **make sure `PostgreSQL` is installed and configured (**_a data source is configured_**) before running the application**.

If test data for the tables is needed, there is a commented class `TestData`, which can be uncommented (it is located is `src/main/java/util`). **The app needs to be restarted for the changes to take effect, i.e. for the data to be inserted into the database.** 

The `SQL` script itself for the test data can be found in the `testdata.sql` file located in `src/main/resources/db`. It can be used to insert the data into the tables manually.