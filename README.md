# Week3_Whiss - Resources - Data-driven Android Applications

This week we will be building a facebook client which displays the home wall user.

Time spent: **50** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can sign in to Facebook using Facebook sdk login (2 points)
* [x] User can view the tweets from their home timeline
* User should be displayed the username, name, and body for each tweet (2 points)
  * [x] User should be displayed the relative timestamp for each tweet "8m", "7h" (1 point)
  * [x] User can view more tweets as they scroll with infinite pagination (1 point)
* User can compose a new tweet
  * [ ] User can click a “Compose” icon in the Action Bar on the top right (1 point)
  * [ ] User can then enter a new tweet and post this to twitter (2 points)
  * [ ] User is taken back to home timeline with new tweet visible in timeline (1 point)

The following **optional** features are implemented:

* [x] While comment a post, user can see a character counter with characters remaining for a comment out of 140 (1 point)
* [x] Links in tweets are clickable and will launch the web browser (see autolink) (1 point)
* [x] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh) (1 point)
* [x] User can open the twitter app offline and see last loaded tweets
* [x] Tweets are persisted into sqlite and can be displayed from the local DB (2 points)
* [x] User can tap a tweet to display a "detailed" view of that tweet (2 points)
* [ ] User can select "reply" from detail view to respond to a tweet (1 point)

The following **bonus** features are implemented:

 * [x] User can see embedded image media within the tweet detail view (1 point)
 * [ ] User can watch embedded video within the tweet (1 point)
 * [ ] Compose activity is replaced with a modal overlay (2 points)
 * [x] Use Parcelable instead of Serializable using the popular Parceler library. (1 point)
 * [x] Leverage RecyclerView as a replacement for the ListView and ArrayAdapter for all lists of tweets. (2 points)
 * [ ] Move the "Compose" action to a FloatingActionButton instead of on the AppBar. (1 point)
 * [x] Replace all icon drawables and other static image assets with vector drawables where appropriate. (1 point)
 * [x] Leverage the data binding support module to bind data into one or more activity or fragment layout templates. (1 point)
 * [x] Replace Picasso with Glide for more efficient image rendering. (1 point)


The following **additional** features are implemented:
 * [x] View full screen image and swipe horizontally to close
## Video Walkthrough

Here's a walkthrough of implemented user stories:

<a href="https://i.imgur.com/Am1B6I7.gif"> <img src="https://i.imgur.com/Am1B6I7.gif" title="GIF_1" /> </a>
<a href="https://i.imgur.com/RiaY4nQ.gif"> <img src="https://i.imgur.com/RiaY4nQ.gif" title="GIF_2" /> </a>
<a href="https://i.imgur.com/LUOMwmC.gif"> <img src="https://i.imgur.com/LUOMwmC.gif" title="GIF_3" /> </a>
<a href="https://i.imgur.com/PRMRaPh.gif"> <img src="https://i.imgur.com/PRMRaPh.gif" title="GIF_4" /> </a>
<a href="https://i.imgur.com/dcSOmoj.gif"> <img src="https://i.imgur.com/dcSOmoj.gif" title="GIF_5" /> </a>
<a href="https://i.imgur.com/4TKkCcf.gif"> <img src="https://i.imgur.com/4TKkCcf.gif" title="GIF_6" /> </a>

GIF created with [AZ Screen Recorder](https://play.google.com/store/apps/details?id=com.hecorat.screenrecorder.free&hl=en).

## Notes


## Open-source libraries used

- [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
- [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit)
- [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
