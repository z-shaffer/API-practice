# API-practice
To create the jar file containing the application, run: mvn clean package. This will compile all your code changes and create a Java jar file with your compiled code.

To create the Docker image, run: docker build -t restful-app-hw4 . This will create an image named restful-app-hw4, using the Dockerfile found in the current directory (which the . represents). If you use a different image name that's fine, but you need to then use it consistently in all the commands given here. I will use restful-app-hw4 in the rest of the steps. If you have a Mac with Apple silicon, see the note above.

To push this image up to Google Cloud Platform, you first need to tag it with an appropriate name. The command for this is docker tag restful-app-hw4 gcr.io/project-id/restful-app-hw4, where project-id is the GCP project id for your project. You can run gcloud config list project to see what this project id is. Next, run docker push gcr.io/project-id/restful-app-hw4 to actually send the tagged image up to the Google Cloud Platform Container Registry.

Finally, you need to deploy the app to Cloud Run. You use the command gcloud run deploy --image gcr.io/project-id/restful-app-hw4 --memory=1Gi restful-app-hw4 to deploy the image. You want to allow unauthenticated access, and you should pick a region in which to deploy your app.
