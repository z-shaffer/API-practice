# API-practice
To create the jar file containing the application, run: mvn clean package. This will compile all your code changes and create a Java jar file with your compiled code.

To create the Docker image, run: "docker build -t restful-app ." This will create an image named restful-app, using the Dockerfile found in the current directory (which the . represents).

To push this image up to Google Cloud Platform, you first need to tag it with an appropriate name. The command for this is docker tag restful-app gcr.io/project-id/restful-app, where project-id is the GCP project id for your project. You can run gcloud config list project to see what this project id is. Next, run docker push gcr.io/project-id/restful-app to actually send the tagged image up to the Google Cloud Platform Container Registry.

Finally, you need to deploy the app to Cloud Run. You use the command gcloud run deploy --image gcr.io/project-id/restful-app --memory=1Gi restful-app to deploy the image. You want to allow unauthenticated access, and you should pick a region in which to deploy your app.
