var Parse = require('parse').Parse;
var config = require('./config.json');
Parse.initialize(config.parse_application_id, config.parse_client_key);

var userQuery = new Parse.Query(Parse.User);
userQuery.equalTo("username","tests");

var pushQuery = new Parse.Query(Parse.Installation);
pushQuery.matchesQuery('owner', userQuery);

// Send push notification to query
Parse.Push.send({
    where: pushQuery,
    data: {
        action: "com.example.UPDATE_STATUS",
        alert: "Call the number",
        phone: "12120000000"
    }
}, {
    success: function() {
        console.log("success");
    },
    error: function(error) {
        console.log(error);
    }
});

