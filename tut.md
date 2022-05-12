# Nexient Bench Elasticsearch 8 tutorial based off [here](https://logz.io/blog/elasticsearch-tutorial/), [here](https://www.baeldung.com/elasticsearch-java) and [here](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html)

## Dependencies

- docker
- python3
- curl
- jdk 11
- maven

## --- Part 1 ---

## Run Elasticsearch in Docker container with Docker compose (recommended)

```bash
# in root dir of project
docker-compose up
```

## OR Run Elasticsearch in standalone Docker container

```bash
docker container run -p 9200:9200 -e "discovery.type=single-node" -e COLUMNS=$(tput cols) -e LINES=$(tput lines) -e ELASTIC_PASSWORD=changeme -i -t elasticsearch:8.2.0
```

### Test Elasticsearch

```bash
curl -fsSlku "elastic:$PASSWORD" https://localhost:9200
```

## OR Create VM with Debian-based Distro

### Install Elasticsearch

```bash
curl -fsSl https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
sudo apt-get install apt-transport-https
echo "deb https://artifacts.elastic.co/packages/6.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-6.x.list
sudo apt-get update
sudo apt-get install elasticsearch
```

### Edit Elasticsearch configuration

```bash
sudo vim /etc/elasticsearch/elasticsearch.yml
network.host: "localhost"
http.port:9200
```

### Start Elasticsearch with systemd

```bash
sudo systemctl start  elasticsearch
```

### Tail Elasticsearch logs

```bash
tail -n +1 -F /var/log/elasticsearch/*.log
```

### Test Elasticsearch

```bash
curl -fsSlku "elastic:changeme" https://localhost:9200
```

## --- Part 2 ---

### Alter cluster settings to automatically create indices

```bash
curl -fsSlku "elastic:changeme" -X PUT -H 'Content-Type: application/json' -d '
{
  "persistent": {
    "action.auto_create_index": "true" 
  }
}
' https://localhost:9200/_cluster/settings
```

### Insert a document

```bash
curl -fsSlku "elastic:changeme" -X POST -H 'Content-Type: application/json' -d '
{
  "@timestamp": "2099-11-15T13:12:00",
  "message": "GET /search HTTP/1.1 200 1070000",
  "user": {
    "id": "kimchy"
  }
}
' https://localhost:9200/my-index-000001/_doc/
```

### Get cluster health

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/_cluster/health | python3 -m json.tool | less
```

### Get cluster stats

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/_cluster/stats | python3 -m json.tool | less
```

### Get all cluster settings

```bash
curl -fsSlku "elastic:changeme" -X GET 'https://localhost:9200/_cluster/settings/?include_defaults=true&flat_settings=true' | python3 -m json.tool | less
```

### A collection of connected nodes is called a cluster.

### Get nodes info

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/_nodes | python3 -m json.tool | less
```

### Get all indices (not JSON output)

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/_cat/indices |  less
```

### Get information on index

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/my-index-000001/ | python3 -m json.tool | less
```

```bash
curl -fsSlku "elastic:changeme" -X GET https://localhost:9200/my-index-000001/_search | python3 -m json.tool | less
```

## --- Part 3 ---

### Add Elasticsearch Java API to maven pom.xml

```xml

<dependencies>
    <dependency>
        <groupId>co.elastic.clients</groupId>
        <artifactId>elasticsearch-java</artifactId>
        <version>8.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.12.3</version>
    </dependency>
</dependencies> 
```

