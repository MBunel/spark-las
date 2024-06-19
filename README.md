# Spark-las

_Spark-las_ is a spark reader for las and laz point cloud files

## Usage

Read a laz file :
```scala
val las_file = spark.read.format("LAS.LAS").load("las_file.laz")
```

Transform a folder of laz files in a parquet file:
```scala
val las_file = spark.read.format("LAS.LAS").load("*.laz")
las_file.write.parquet("point_cloud.parquet")
```

## Features
- [x] Read las files:
  - [x] Las 1.0-1.4
  - [X] Point format 0 to 10
  - [X] Files compressed with laszip (laz)
  - [ ] Predicates Pushdown for spatial query and return number
  - [ ] COPC optimisations
- [ ] Write las and laz files

## Installation
Based on the *data-validator*  [README.md](https://github.com/target/data-validator/?tab=readme-ov-file#retrieving-official-releases-via-direct-download-or-maven-compatible-dependency-retrieval-eg-spark-submit).

### From GitHub Maven repository
- Create a GitHub user token with the _read:packages_ authorisation.
- Create a ivy configuration file :
```xml
<ivysettings>
    <settings defaultResolver="thechain">
        <credentials
            host="maven.pkg.github.com"
            username="YOUR USER NAME"
            passwd="YOUR GITHUB TOKEN"
            realm="GitHub Package Registry"/>
    </settings>
    <resolvers>
        <chain name="thechain">
            <ibiblio name="central" m2compatible="true" 
                root="https://repo1.maven.org/maven2/" />
            <ibiblio name="ghp-dv" m2compatible="true" 
                root="https://maven.pkg.github.com/mbunel/spark-las"/>
        </chain>
    </resolvers>
</ivysettings>
```
- Call this file at the spark startup with this option : `--conf spark.jars.ivySettings=$(pwd)/my_ivy.settings` 
- If necessary, set a proxy: `--driver-java-options '-Dhttp.proxyHost=proxy.ign.fr -Dhttp.proxyPort=3128 -Dhttps.proxyHost=proxy.ign.fr-Dhttps.proxyPort=3128'`

### Directly with jars
- Download directly the jar file: https://github.com/MBunel/spark-las/packages/2182443
- Use the ```--jars``` option at spark startup