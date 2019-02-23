# Skyfall
A beautiful android weather app for Sweden. Skyfall acquires data from [SMHI Open Data API](https://opendata-download-metfcst.smhi.se/pmp3gv2). The API documentation can be found [here](https://opendata.smhi.se/apidocs/metfcst/) and is [licensed](https://www.smhi.se/klimatdata/oppna-data/information-om-oppna-data/villkor-for-anvandning-1.30622) under [Creative commons Erkännande 4.0 SE](http://www.creativecommons.se/wp-content/uploads/2015/01/CreativeCommons-Erk%C3%A4nnande-4.0.pdf). 

## Software Design Pattern
The design pattern of this project is attempting to conform with the MVP design pattern, although not to a full extent as dependency injection is not used. Therefore all context related operations are handled by view, like for instance acquiring user location, while model handles operations like network requests using Retrofit and data formatting.

## Libraries used
* [Retrofit](https://square.github.io/retrofit/)
* [SunriseSunset](https://github.com/caarmen/SunriseSunset)
* [Gson](https://github.com/google/gson)
* [Weather Icons](https://github.com/erikflowers/weather-icons)