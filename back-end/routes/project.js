const express = require('express');
const router = express.Router();
const user = require ('../Controllers/User');
const Project= require('../Controllers/Project');
const Utility = require('../Common/utility.js')
let crypto = require("crypto");
const UserModel = require ('../Model/User');
const projectPartnerModel=require('./../Model/Project_partner')
const ProjectModel =require('./../Model/Project')
router
.get('/',(req, res)=>{
  console.log("ok");
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      Project.getListProject(user._id,(err, result)=>{
        if (err)
        {
          res.status(404).json({
            result: false,
            detail: "query error"
          });
        }
        else
        {
          res.json({"detail":result});
        }
      })
    }
    else {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }

  })

})
.get("/my/:id", async (req, res, next) => {
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user) {
      // if (req.headers._id) {
      if (1==1){
        Project.getInfo(user._id, req.params.id, (err, result) => {
          if (err) {
            res.status(404).json({
              result: false,
              detail: "query error"
            });
          } else {
            res.status(200).json({
              result: true,
              detail: result
            });
          }
        })
      } else {
        res.status(404).json({
          result: false,
          detail: "Query error none id"
        });
      }
    } else {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })
})
.post("/delete",async(req,res)=>{
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      Project.deleteProject(user._id,req.body.id,(err,result)=>{
        if (err)
        {
          res.json({
            result: false,
            detail: "query error"
          });
        }
        else
        {
          res.json({
            "result":true,
            "detail":result
          })
        }
      })
    }
    else{
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })

})
.post("/done",async(req,res)=>{
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      Project.changStatusDoneProject(user._id,req.body.id,(err,result)=>{
        if (err)
        {
          res.json({
            result: false,
            detail: "query error"
          });
        }
        else
        {
          res.json({
            "result":true,
            "detail":result
          })
        }
      })
    }
    else{
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })

})
.post("/", async (req, res, next) => {
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user) {
      if (req.body.title && req.body.description &&req.body.deadline) {
        
          Project.createProject(req.body.title,req.body.description,req.body.deadline, user, (err, result) => {
            if (err) {
              res.status(404).json({
                result: false,
                detail: "query error"
              });
            } else {
              res.status(200).json({
                result: true,
                detail: result
              });
            }
          // })

        })
     
      } else {
        res.status(404).json({
          result: false,
          detail: "Query error"
        });
      }
    } else {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })
})
.post('/getProjectPartner',(req,res)=>{
  Utility.verifyToken(req.headers.token, (err, user)=>{
    if (user){
      Project.getProjectPartner(user._id,(err,result)=>{
        if (err)
        {
          res.json ({result:false})
        }
        else
        {
          res.json({result:true,detail:result})
        }
      })
    }
    else{
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })
})
.post("/partner",async (req,res,next)=>{
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      Project.addPartNer(req.body.id, req.body.email,user.name,(err,result)=>{
        if (err)
        {
          res.json ({result:false})
        }
        else
        {
        
            res.json({result:true,detail:result})
          
        }
      })
    }
    else
    {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })
})

.post('/partner/delete',(req,res)=>{
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      UserModel.findById(user._id,(err,result)=>{
        if (result)
        {
          for (i=0;i<result.projectPartner.length;i++)
          {
            if (result.projectPartner[i]._id==req.body.id)
            {
              result.projectPartner[i].deleted=true;
              result.save();
              res.json({
                result: true,
                detail: result.projectPartner
              });

                break;
            }
           
          }

            
        }
        else
        {
          res.json({
            result: false,
            detail: "Query error"  
          });
        }


      })
      



    }
    else
    {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
    
  })
})
.post('/partner/info',(req,res)=>{
  Utility.verifyToken(req.headers.token, (err, user) => {
    if (user)
    {
      Project.getInfoProjectPartner(req.body.id,(err, result)=>{
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
    }
    else
    {
      res.status(401).send({
        result: false,
        detail: "UnAuthorized"
      });
    }
  })

})
  .post("/task", async (req, res, next) => {
   
    Utility.verifyToken(req.headers.token, (err, user) => {
      if (user) {
        if (
          req.body.id &&
          req.body.title&&
          req.body.deadline 
           ) {
          // Cloudinary.upload(newQuestion.img, (err, url) => {
          //   newQuestion.img = url;
          Project.addTask(req.body.id,req.body.title,req.body.deadline, user._id, (err, result) => {
              if (err) {
                res.status(404).json({
                  result: false,
                  detail: "query error"
                });
              } else {
                res.status(200).json({
                  result: true,
                  detail: result.task
                });
              }
            })
          // })
        } else {
          res.status(404).json({
            result: false,
            detail: "Query error"
          });
        }
      } else {
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"
        });
      }
    })
  })
  .post('/task/list',(req, res)=>{
    console.log ("ok");
    Utility.verifyToken(req.headers.token, (err, user)=>{
      if (user)
      {
        Project.getTaskInProject(user._id,req.body.id, (err, result)=>{
          if (err)
          {
            res.json({
              result: false,
              detail: "query error"
            });
          }
          else
          {
            res.json({
              detail: result
            });
          }
        })
      }
      else
      {
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"
        });
      }
    })
  })
  .post ('/tasks',(req,res,next)=>{
    ProjectModel.findById(req.body.id,(err, result)=>{
      if (result)
      {
        console.log (result)
        res.json({detail:[result]})
      }
      else
      {
        res.json({
          result: false,
          detail: "query error"
        });
      }
    })
  })
  .get('/task/item',(req, res, next)=>{
    let newTask = req.body.newTask;
    Utility.verifyToken(req.headers.token, (err, user) => {
      if (user)
      {
        Project.getTask(newTask,user._id,(err, result)=>{
          if (err)
          {
            res.status(404).json({
              result: false,
              detail: "query error"
            });
          }
          else{
            res.status(200).json({
              result: true,
              detail: result.task
            });
          }
        })
      }
      else
      {
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"
        });
      }
    })

  })
  .delete("/task", async (req, res, next) => {
    let newTask = req.body.newTask;
    Utility.verifyToken(req.headers.token, (err, user) => {
      if (user) {
        if (
          newTask._id 
           ) {
          // Cloudinary.upload(newQuestion.img, (err, url) => {
          //   newQuestion.img = url;
          Project.deleteTask(newTask, user._id, (err, result) => {
              if (err) {
                res.status(404).json({
                  result: false,
                  detail: "query error"
                });
              } else {
                res.status(200).json({
                  result: true,
                  detail: result.task
                });
              }
            })
          // })
        } else {
          res.status(404).json({
            result: false,
            detail: "Query error"
          });
        }
      } else {
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"
        });
      }
    })
  })
  .post('/task/additem',async (req,res, next)=>{
    
      Project.addItemTasks(req.body.title,req.body.idTask,req.body.idProject, (err, result)=>{
        if (err)
        {
          res.status(404).json({
            result: false,
            detail: "query error"
          });
        }
        else{
          res.status(200).json({
            result: true,
            detail: "s"
          });
        }
      })
   
  })
  .post('/task/item', async (req, res, next)=>{
    Utility.verifyToken(req.headers.token, (err, user)=>{
        if (user){
          Project.addItemTask(req.body.title,req.body.idTask,user._id,req.body.idProject, (err, result)=>{
            if (err)
            {
              res.status(404).json({
                result: false,
                detail: "query error"
              });
            }
            else{
              res.status(200).json({
                result: true,
                detail: "s"
              });
            }
          })
        }
        else
        {
          res.status(401).send({
            result: false,
            detail: "UnAuthorized"})
        }
    })
  })
  .post('/task/item/movenext',(req,res)=>{
    Utility.verifyToken(req.headers.token, (err, user)=>{
      if (user){
        Project.moveItemTaskNext(req.body.id, req.body.idTask,user._id,req.body.idProject,(err,result)=>{
          if (err)
          {
            res.json({"result":false})
          }
          else
          {
            res.json({"result":true})
          }
        })
      }
      else{
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"})
      }
    })
  })
  .post('/task/item/moveprev',(req,res)=>{
    Utility.verifyToken(req.headers.token, (err, user)=>{
      if (user){
        Project.moveItemTaskPrev(req.body.id, req.body.idTask,user._id,req.body.idProject,(err,result)=>{
          if (err)
          {
            res.json({"result":false})
          }
          else
          {
            res.json({"result":true})
          }
        })
      }
      else{
        res.status(401).send({
          result: false,
          detail: "UnAuthorized"})
      }
    })
  })

module.exports=router;
