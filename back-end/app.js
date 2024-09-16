const express = require("express");
const app = express();
const server = require("http").createServer(app)
var io= require ("socket.io").listen(server)
var fs = require("fs")
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
var nodemailer = require('nodemailer');


require("dotenv/config");

//Routes
app.use (bodyParser.urlencoded());
app.use (bodyParser.json())

const postRoute = require ('./routes/user');
app.use ("/user", postRoute);

const projectRoute = require ('./routes/project');
app.use ("/project", projectRoute);

app.post("/email", (req,res)=>{
  var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'tase.app@gmail.com',
      pass: '150598bd'
    }
  });
  
  var mailOptions = {
    from: 'tase.app@gmail.com',
    to: req.body.email,
    subject: 'TASE - Bạn có một dự án '+ req.body.title+'được chia sẽ bởi '+req.body.username,
    html: '<h2>Dự án '+ req.body.title+'</h2><p>Bạn vui lòng kiểm tra trong phần Group</p><h3>Thanks & B. Regards, TASE(Ms)</h3> <p>==================</p> <p>Address: HUTECH.</p> <p>Tell: 0989837xxx Email: TASE.APP@gmail.com</p>'

  };
  
  
  transporter.sendMail(mailOptions, function(error, info){
    if (error) {
      res.json ({result:false})
      console.log('Email sent: ' +error);


    } else {
      console.log('Email sent: ' + info.response);
      res.json ({result:true,detail:info.response})

    }
  });

})

app.use(bodyParser.json());

io.sockets.on ("connection",function(socket){
  console.log ("Connecting " + socket.id);
  socket.on ("reset", function(data){
    console.log ("c"+data);
    io.sockets.emit ("n", {noidung:data});
  })

})



//Connect Db

// DB_CONNECTION=mongodb+srv://quansean:150598bd!@tesa-e6rwo.mongodb.net/test?retryWrites=true&w=majority

mongoose.connect(
  process.env.DB_CONNECTION,
  { useNewUrlParser: true },
  (err, success) => {
    if (err) {
      console.log(err);
    } else {
      console.log("Connected mongodb huy");
    }
  }
);

server.listen(process.env.PORT || 2409,(err, success)=>{
    if (err)
    {
        console.log (err)
    }
    else 
    {
        console.log ("Listen sss")
    }
    

});




