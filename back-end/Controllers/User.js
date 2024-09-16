const express = require('express');
const router = express.Router();
const User = require ('../Model/User');
const Project= require('../Model/Project');
const Utility= require ('../Common/utility');
let crypto = require("crypto");
var nodemailer = require('nodemailer');


let isExistEmail = (email) => {
    return new Promise((resolve, reject) => {
      User.find({
        "email": email,
        "deleted": false
      }, (err, res) => {
        if (err) {
          resolve(false)
        }
        if (res.length == 0) {
          resolve(true);
        } else
          resolve(false);
      });
    })
  }

  let user = {
    register: async (email, password, name, callback) => {
      email = email.toLowerCase();
      let isEmailUseable = await isExistEmail(email);
      if (!isEmailUseable) {
        return callback(new Error(ERROR.DUPLICATE), null);
      } else {
        password = crypto.createHash('sha256').update(password).digest('hex');
        let newUser = new User({
          email: email,
          name: name,
          password: password,
          deleted: false,
          token:'',
          key_repass:''
        });
        newUser.save()
          .then(res => callback(null, res))
          .catch(err => callback(err, null));
      }
    },
    login: async (email, password, callback) => {
      password = crypto.createHash('sha256').update(password).digest('hex');
      email = email.toLowerCase();
      User.findOne({
        email: email,
        password: password,
        deleted: false
      }, (err, res) => {
        if (res != null) {
          let token = Utility.getToken(res.email);
          if (token) {
            return callback(null, { user: { ...res._doc }, token: token[0] });
          } else {
            Utility.computingJWT(email, (err, newToken) => {
              Utility.addNewTokenForUser(email, newToken);
              return callback(null, { user: { ...res._doc }, token: newToken });
            })
          }
        } else {
          callback(err, null);
        }
      })
    },
    createKeyRePass: async(email,callback)=>{
      User.findOne({email:email},(err, result)=>{
        if (err)
        {
          callback(true, null)
        }
        else
        {


          if (result==null)
          {
            callback(true, null)
          }
          else
          {
            var x = Math.floor(Math.random() * 100)*12;
            if (x==0)
            {
              x=3485;
            }
            if (x<100)
            {
              x=x*120;
            }
            else
            {
              if (x<1000)
                {
                  x=x*12
                }
              
            }
            var transporter = nodemailer.createTransport({
              service: 'gmail',
              auth: {
                user: 'tase.app@gmail.com',
                pass: '150598bd'
              }
            });
            
            var mailOptions = {
              from: 'tase.app@gmail.com',
              to: email,
              subject: 'TASE - Yêu cầu thay đổi mật khẩu',
              html: '<h2>Chào quý khách</h2><p>TASE dã nhận được yêu cầu thay đổi mật khẩu của quý khách.</p><p>Key thay đổi mật khẩu của bạn là: </p><h3>'+x+'</h3><h3>Thanks & B. Regards, TASE(Ms)</h3> <p>==================</p> <p>Address: HUTECH.</p> <p>Tell: 0989837xxx Email: TASE.APP@gmail.com</p>'
          
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
            result.key_repass=x;
            result.save()
            .then(res => callback(null, res))
            .catch(err => callback(err, null));

          }
        }
      })
    },
    changePassword:(email, key, pass, callback)=>{
      User.findOne({email:email,key_repass:key},(err,result)=>{
        if (err)
        {
          callback(true, null)
        }
        else
        {
          if (result==null)
          {
            callback(true,null)
          }
          else
          {
            pass = crypto.createHash('sha256').update(pass).digest('hex');
            result.password=pass
            result.save()
            .then(res => callback(null, res))
            .catch(err => callback(err, null));

          }
        }

      })
    },
    logout: async (token, callback) => {
      User.findOneAndUpdate({
        "token": token
      }, { "token": "" }, callback);
    }
    ,
    updateInfo: (user, callback) => {
      user.password = crypto.createHash('sha256').update(user.password).digest('hex');
      User.findById(user._id, (err, oldUser) => {
        oldUser.name = user.name;
        oldUser.password = user.password;
     
        oldUser.save()
          .then(res => callback(null, res))
          .catch(err => callback(err, null));
      })
    },
    deleteAccount: (token, callback) => {
      User.findOne({
        "token": token
      }, (err, doc) => {
        if (doc) {
          doc.token = "";
          doc.deleted = true;
          doc.save()
            .then(res => callback(null, res))
            .catch(err => callback(err, null));
        } else {
          callback(true, null);
        }
      });
    }
  }
module.exports=user