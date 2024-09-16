const mongoose = require ('mongoose');
const PostSchema= mongoose.Schema({
    _id:Number,
    title:String,
    done:Boolean,
    deleted:Boolean
})
module.exports = mongoose.model ('Item', PostSchema)