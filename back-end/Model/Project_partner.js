const mongoose = require ('mongoose');
let Schema = mongoose.Schema,
ObjectId = Schema.ObjectId;
const PostSchema= mongoose.Schema({
    id:ObjectId,
    author : String,
    // client: [User.schema],
    title:String,
    description:String,
    deadline: Date,
    done:Boolean,
    deleted: Boolean,
})
module.exports = mongoose.model ('Project_partner', PostSchema)