FROM gcr.io/google_appengine/openjdk
# Replace occurrences of YOUR_ARTIFACT_NAME_HERE with the name of the deployed jar
target/bananapp-264116-1.0-SNAPSHOT.jar /app/
ENTRYPOINT ["/docker-entrypoint.bash"]
CMD ["java","-jar","/app/bananapp-264116-1.0-SNAPSHOT.jar"]
