#todo-list
第二章 项目实战
* 删除课程
<pre>
Request URL: /content/teachplan/246
课程计划添加成功，如果课程还没有提交时可以删除课程计划。
1.删除大章节，大章节下有小章节时不允许删除。
2.删除大章节，大单节下没有小章节时可以正常删除。
3.删除小章节，同时将关联的信息进行删除。
</pre>
* 课程计划排序
<pre>
Request URL: http://localhost:8601/api/content/teachplan/movedown/43
Request Method: POST
Request URL: http://localhost:8601/api/content/teachplan/moveup/43
Request Method: POST
向上移动后和上边同级的课程计划交换位置，可以将两个课程计划的排序字段值进行交换。
向下移动后和下边同级的课程计划交换位置，可以将两个课程计划的排序字段值进行交换。
</pre>
* 师资管理
<pre>
CURD
只允许向机构自己的课程中添加老师、删除老师。
</pre>
* 删除课程
<pre>
delete  /course/87
课程的审核状态为未提交时方可删除。
删除课程需要删除课程相关的基本信息、营销信息、课程计划、课程教师信息。
</pre>
第三章 项目实战
<pre>
//todo 视频合并分块 前端http canceled 原因未知
//todo 分布式任务处理 补偿机制
//todo 视频处理未测试
//todo 绑定媒资未测试
</pre>
* 实现媒资解除绑定功能
<pre>
delete /teachplan/association/media/{teachPlanId}/{mediaId}
返回200状态码表示成功。
</pre>