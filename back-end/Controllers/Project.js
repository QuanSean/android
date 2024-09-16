const projectModel =require ('../Model/Project');
const taskModel= require('../Model/Task');
const item= require('./../Model/Item')
const userModel = require ('./../Model/User')
const projectPartnerModel = require ('./../Model/Project_partner')

 

let project={
  //PROJECT
  getListProject:(idUser,callback)=>{
    projectModel.find({_idUser:idUser},callback )
  },
    getInfo: (idUser, idProject, callback) => {
      projectModel.findOne({
          _idUser: idUser,
          _id: idProject
        }, callback)
      },
      createProject: (title,description,deadline,user, callback) => {
        let newProject = new projectModel({
            _idUser: user._id,
            title: title,
            description: description,
            done:false,
            deadline:deadline,
            task:[],
            deleted: false
        });
        
        newProject.save()
          .then(res => callback(null, res))
          .catch(err => callback(err, null))
      },
      deleteProject:(idUser,id,callback)=>{
        projectModel.findOneAndUpdate({_id:id},{$set:{'deleted':true}},{new: true},(err,result)=>{
          if(err){
            callback(true,null) 
        } else{
            callback(false,result)
        }
            
        })
      },
      changStatusDoneProject:(idUser,id,callback)=>{
        projectModel.findOneAndUpdate({_id:id},{$set:{'done':true}},{new: true},(err,result)=>{
          if(err){
            callback(true,null) 
        } else{
            callback(false,result)
        }
            
        })
      },
      //TASK
      getTaskInProject:(idUser, idProject,callback)=>{
        projectModel.find({_idUser:idUser,_id:idProject},callback)

      },
      getTask:(nTask, idUser, callback)=>{
        projectModel.findOne ({_id:nTask._id,_idUser:idUser},callback)
      },
      deleteTask:(nTask, idUser, callback)=>{
        projectModel.findById(nTask._id, (err, fProject)=>{
          if (fProject._idUser.toString() != idUser) {
            callback(true, null);
          } 
          else
          {
            let newTask = new taskModel({  
              // _id:fProject.task.length + 1,
              // title:nTask.title,
              // deadline: nTask.deadline,
              // done: false,
              delete: true
            });
            
            fProject.task.push(newTask);
            fProject.save()
              .then(res => callback(null, res))
              .catch(err => callback(err, null));
          }
        })
      },
      addItemTask:(title, idTask, idUser, idProject, callback)=>{
        projectModel.findById (idProject,(err, fProject)=>{
          if (fProject._idUser.toString()!=idUser)
          {
            callback(true, null);
          }
          else
          {
            // fProject.task.findById(idTask, (err, fTask)=>{
            //   if (!fTask)
            //   {
            //     callback(true, null);
            //   }
            //   else
            //   {
                let newItem= new item ({
                  _id: fProject.task[idTask-1].item.length+1,
                  title:title,
                  done:false,
                  deleted:false});
                  console.log ("nwe"+newItem);
                  fProject.task[idTask-1].item.push(newItem)
                  fProject.save()
                .then(res => callback(null, res))
                .catch(err => callback(err, null));
            //   }
            // })
            // console.log (fProject.task[0].item.length);
          }
        })

      }, 
      addItemTasks:(title, idTask, idProject, callback)=>{
        projectModel.findById (idProject,(err, fProject)=>{
          
            if (fProject)
            {
              let newItem= new item ({
                _id: fProject.task[idTask-1].item.length+1,
                title:title,
                done:false,
                deleted:false});
                console.log ("nwe"+newItem);
                fProject.task[idTask-1].item.push(newItem)
                fProject.save()
              .then(res => callback(null, res))
              .catch(err => callback(err, null));
            }
            else
            {
              callback(true,null)
            }
          
        })

      },     
      addPartNer:( idProject,email,author,callback)=>{
        projectModel.findById( idProject,(err,result)=>{
          if (err)
          {
            callback(err,null);
          }
          else
          {
            userModel.find({email:email},(errr,resultUser)=>{
                if (resultUser)
                {
                  if (resultUser.length==0)
                  {
                    callback(err,[]);
                  }
                  else
                  {
                    pppp = new projectPartnerModel ({
                      id:result._id,
                      author : author,
                      // client: [User.schema],
                      title:result.title,
                      description:result.description,
                      deadline: result.deadline,
                      done:result.done,
                      deleted: result.deleted,
                    })

                    console.log (pppp)
  
                    resultUser[0].projectPartner.push(pppp);
                    
                    resultUser[0].save()
                      .then(res => callback(null, res))
                      .catch(err => callback(err, null));
                  }
                  
                }
                else
                {
                  callback(errr,null)
                }
            
              }
            )
          }
        })
        // userModel.find({email:email},(err,result)=>{
        //   if (err)
        //   {
        //     callback(err,null)
        //   }
        //   else
        //   {

        //   }
        // })
      }, 
      getInfoProjectPartner:(id,callback)=>{
        projectModel.findOne({_id:id},(err,result)=>{
          if (err)
          {
            callback(true,null)
          }
          else
          {
            callback(false,result)
          }
        })
      },
      getProjectPartner:(idUser,callback)=>{
        userModel.findById(idUser,(err,result)=>{
          if (err)
          {
            callback (err,null)
          }
          else
          {
            callback(false,result.projectPartner)
          }
        })

      }, 
      moveItemTaskPrev:(id,idTask, idUser, idProject, callback)=>{
        projectModel.findById (idProject,(err, fProject)=>{
          if (fProject._idUser.toString()!=idUser)
          {
            callback(true, null);
          }
          else
          {


            var a=0;
            for (i=fProject.task.length-1;i>=0;i--)
            {
              console.log(fProject.task[fProject.task.length-1]._id)
              
              if (fProject.task[i]._id==idTask)
              {
                if (i==0)
                {
                  callback(true, null);
                }
                else
                {
                  for (j=0;j<fProject.task[i].item.length;j++)
                  {
                    if (fProject.task[i].item[j]._id==id)
                    {
                      var itemMove= new item({
                        _id:fProject.task[i-1].item.length+1,
                        title:fProject.task[i].item[j].title,
                        done:fProject.task[i].item[j].done,
                        deleted:false
                      });

                      console.log(fProject.task[i].item[j])
                      var itemDelete= new item({
                        _id:fProject.task[i].item[j]._id,
                        title:fProject.task[i].item[j].title,
                        done:fProject.task[i].item[j].done,
                        deleted:true
                      });
                      
                      fProject.task[i].item[j]=itemDelete;
                      fProject.task[i-1].item.push(itemMove)
                      fProject.save()
                      .then(res => callback(null, res))
                      .catch(err => callback(err, null));
  
                    }
                  }
                }


              }
            }

          }
        })


      }
      ,  
      moveItemTaskNext:(id,idTask, idUser, idProject, callback)=>{
        projectModel.findById (idProject,(err, fProject)=>{
          if (fProject._idUser.toString()!=idUser)
          {
            callback(true, null);
          }
          else
          {


            var a=0;
            for (i=0;i<fProject.task.length;i++)
            {
              if (fProject.task[i]._id==idTask)
              {
                if (i==fProject.task.length-1)
                {

                }
                else
                {
                  for (j=0;j<fProject.task[i].item.length;j++)
                  {
                    if (fProject.task[i].item[j]._id==id)
                    {
                      var itemMove= new item({
                        _id:fProject.task[i+1].item.length+1,
                        title:fProject.task[i].item[j].title,
                        done:fProject.task[i].item[j].done,
                        deleted:false
                      });

                      console.log(fProject.task[i].item[j])
                      var itemDelete= new item({
                        _id:fProject.task[i].item[j]._id,
                        title:fProject.task[i].item[j].title,
                        done:fProject.task[i].item[j].done,
                        deleted:true
                      });
                      
                      fProject.task[i].item[j]=itemDelete;
                      fProject.task[i+1].item.push(itemMove)
                      fProject.save()
                      .then(res => callback(null, res))
                      .catch(err => callback(err, null));
  
                    }
                  }
                }


              }
            }

          }
        })


      },
      addTask: (id,title,deadline, idUser, callback) => {
        projectModel.findById(id, (err, fProject) => {
          if (fProject._idUser.toString() != idUser) {
            callback(true, null);
          } 
          else {
            let newTask = new taskModel({  
              _id:fProject.task.length + 1,
              title:title,
              deadline: deadline,
              done: false,
              delete: false,
              item:[]
              
            });
            
            fProject.task.push(newTask);
            fProject.save()
              .then(res => callback(null, res))
              .catch(err => callback(err, null));
          }
        })
      }
}

module.exports = project;