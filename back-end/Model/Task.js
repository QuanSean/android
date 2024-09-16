const mongoose = require ('mongoose');
let Schema = mongoose.Schema,
ObjectId = Schema.ObjectId;
const item= require('./Item');
const PostSchema= mongoose.Schema({
    _id:Number,
    title:String,
    deadline: Date,
    done: Boolean,
    delete: Boolean,
    item:[item.schema]
})
module.exports = mongoose.model ('Tasks', PostSchema)