const mongoose = require ('mongoose');
const User=require('./User');
const Task=require('./Task')
let Schema = mongoose.Schema,
ObjectId = Schema.ObjectId;
const PostSchema= mongoose.Schema({
    _idUser : ObjectId,
    // client: [User.schema],
    title:String,
    description:String,
    deadline: Date,
    done:Boolean,
    task:[Task.schema],
    deleted: Boolean,

})
module.exports = mongoose.model ('Project', PostSchema)
