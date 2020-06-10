bananapp, Google App Engine backend
==================

This is the second server of bananapp, a war-packaged and servlet-based backend for redirecting the ajax calls. It receives requests from the first server of bananapp, calls the Google Cloud Platform (more specifically - the custom bananapp AutoML model), and answers with the response (utilizing Google Image Labeling) - the age of a banana and the level of certainty.
