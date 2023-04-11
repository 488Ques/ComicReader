Comics collection: This collection will hold all the comic documents in your app. Each comic document will have the following fields:
- Title: The title of the comic.
- Author: The author of the comic.
- Genre: The genre of the comic, such as action, romance, or horror.
- Image URL: The URL of the cover image for the comic.
  - Chapters subcollection: This subcollection will hold all the chapters for the comic. Each chapter document will have the following fields:
    - Chapter number: The number of the chapter.
    - Title: The title of the chapter.
    - Creation date: The timestamp when the chapter is created.
    - Image URLs: An array of URLs for the pages in the chapter.

Users collection: This collection will hold all the user documents in your app. Each user document will have the following fields:
- Email: The email address of the user.
- Username: The username of the user.
- Favorites subcollection: This subcollection will hold all the comic documents that the user has favorited. Each favorite document will simply be a reference to the corresponding comic document in the Comics collection.
- Reads subcollection: This subcollection will hold all the chapter documents that the user has read. Each read document will be a reference to the corresponding chapter document in the Chapters subcollection of the corresponding comic document in the Comics collection.