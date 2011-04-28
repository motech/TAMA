class LogTagLib {
  def logMsg = { attrs, body ->
     if (attrs['level'] != null) {
       String logLevel = org.apache.log4j.Level
         .toLevel(attrs['level'])
         .toString().toLowerCase()
      log."$logLevel"(body())
     }
     else {
       log.debug(body())
     }
   }
}