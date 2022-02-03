# loan-transaction-api
AAFC DINA loan-transaction implementation.

See DINA loan-transaction [specification](https://github.com/DINA-Web/loan-transaction-specs).

## To Run

For testing purpose a [Docker Compose](https://docs.docker.com/compose/) example file is available in the `local` folder.
Please note that the app will start without Keycloak and in `dev` mode.

Create a new docker-compose.yml file and .env file from the example file in the local directory:

```
cp local/docker-compose.yml.example docker-compose.yml
cp local/*.env .
```

Start the app (default port is 8086):

```
docker-compose up
```

Once the services have started you can access metadata at `http://localhost:8086/api/v1/transaction`

Cleanup:
```
docker-compose down
```

