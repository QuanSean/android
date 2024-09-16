const express = require('express');
const router = express.Router();
const user = require ('../Controllers/User');
const Project= require('../Model/Project');
const Utility = require('../Common/utility.js')
let crypto = require("crypto");

const UserModel = require ('../Model/User');

router
  //verify data before call this api
  .get('/demo',(req, res)=>{
    Project.find({"_id":"5d4564f47bda832d7e31dc88"}, (err,r)=>{
      if (err)
      {
        res.json({result:false})
      }
      else
      {
        res.json({result:true, detail:r})
      }
    })
  })
  .post("/register", (req, res, next) => {
    if (!req.body.email || !req.body.password) {
      res.send({
        result: false
      })
    } else {
      user.register(req.body.email, req.body.password, req.body.name, (error, result) => {
        if (error) {
          res.send({
            result: false,
            detail: error
          });
        } else {
          res.send({
            result: true
          })
        }
      })
    }
  })
  .post("/login", (req, res, next) => {
    user.login(req.body.email, req.body.password, (error, result) => {
      if (error || !result) {
        res.status(200).send({
          result: false
        });
      } else {
        // user.updateToken(req.body.email, req.body.password, result);
        delete result.user.password;
        res.status(200).send({
          result: true,
          token: result.token,
          info: result.user
        });
      }
    })
  })
  .post("/logout", (req, res, next) => {
    // user.logout(req.headers.token, (error, result) => {
    //   // console.log(error +  " "+result);
    //   if (error || !result) {
    //     res.status(404).send({
    //       result: false
    //     });
    //   } else {
    //     res.status(200).send({
    //       result: true
    //     });
    //   }
    // })
    UserModel.findOneAndUpdate({"token":req.headers.token},({"token":""}))
  })
    //demo
  .get("/info",async(req, res, next)=>{
    if(req.headers.token){
      Utility.verifyToken(req.headers.token, (err, user) => {
        user = user._doc;
        console.log (user)
        if (user) {
          res.status(201).json({

            info: user
          })
        } else {
          res.status(401).send({
            result: false
          })
        }
      });
    }

  })
  .post("/verify", async (req, res, next) => {
    if(req.headers.token){
      Utility.verifyToken(req.headers.token, (err, user) => {
        user = user._doc;
        if (user) {
          delete user.password;
          res.status(201).json({
            result: "success verify",
            token: req.headers.token,
            info: user
          })
        } else {
          res.status(401).send({
            result: false
          })
        }
      });
    }
  })
  // .get("/info", async (req, res, next) => {
  //   let verifyToken = await Utility.verifyToken(req.headers.token);
  //   if (verifyToken) {
  //     res.status(200).json({
  //       result: true,
  //       detail: verifyToken
  //     });
  //   } else {
  //     res.status(401).send({
  //       result: false,
  //       detail: "UnAuthorized"
  //     });
  //   }
  // })
  .post("/info",async(req, res, next)=>{
    if(req.headers.token){
      Utility.verifyToken(req.headers.token, (err, user) => {
        user = user._doc;
        if (user) {
          UserModel.findById(user._id,(err, newUser)=>{
            newUser.name = req.body.name;
            newUser.password = crypto.createHash('sha256').update(req.body.password).digest('hex');
            newUser.save();
           
            res.status(200).json({result:true})
            
              
           
          })
        } else {
          res.status(401).send({
            result: false
          })
        }
      });
    }

  })
  .post('/key',(req,res)=>{
    user.createKeyRePass(req.body.email,(err,result)=>{
      if (result)
      {
        res.json({
          result:true,
          detail:result
        })
      }
      else
      {
        res.json({
          result: false,
          detail: "Query error"
        });
      }
    })
  })
  .post('/changePassword',(req, res,next)=>{
    user.changePassword(req.body.email, req.body.key, req.body.pass,(err,result)=>{
      if (err)
      {
        res.json({
          result: false,
          detail: "Query error"
        });
      }
      else
      {
        res.json({
          result:true,
          detail:result
        })
      }
    })
  })
  .post("/name", (req,res)=>{
    Utility.verifyToken(req.headers.token, (err, user) => {
      if (user)
      {
        res.json({result:true,name:user.name})
      }
      else
      {
        res.status(401).send({
          result: false
        })
      }
    })

  })
 
  .delete("/delete", (req, res, next) => {
    // user.deleteAccount(req.headers.token, (error, result) => {
    //   if (error) {
    //     res.status(401).json({
    //       result: false,
    //       detail: "UnAuthorized"
    //     });
    //   } else {
    //     res.status(200).json({
    //       result: true,
    //       detail: "Deleted"
    //     });
    //   }
    // })
    UserModel.findOne({
      "token": req.headers.token
    }, (err, doc) => {
      if (doc) {
        // doc.token = "";
        // doc.deleted = true;
        // doc.save()
        console.log (doc)
          
      } else {
        console.log ("err")

      }
    });

  })





module.exports=router;
