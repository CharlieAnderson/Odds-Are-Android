Parse.Cloud.beforeSave("UserPrivateData", function(request, response) {
    Parse.Cloud.useMasterKey();
    var userPrivateData = request.object;
    if(userPrivateData.isNew() || userPrivateData.dirty("facebookFriends")) {
        console.log("Update friend list.");
        var facebookFriends = userPrivateData.get("facebookFriends");
        var friendsQuery = new Parse.Query(Parse.User);
        friendsQuery.containedIn("facebookID", facebookFriends);
        friendsQuery.find({
            success: function(results) {
                var friends = results;
                userPrivateData.set("friends", friends);
                response.success();
            },
            error: function(error) {
                console.log(error);
                response.error(error);
            }
        });
    } else {
        response.success();
    }
});