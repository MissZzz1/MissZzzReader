# MissZzzReader
大钊阅读  完全免费的小说搜索阅读app
 


#### 通过 抓取第三方小说网站的html页面，来实现小说的搜索以及内容获取，摆脱了庞大累赘的小说数据库，阅读功能仿照掌阅实现。
#### 提供各项阅读设置，目录、跳章、阅读进度、上下翻页、字体、字号、阅读背景、亮度、简繁体切换、自动滚屏、离线缓存等等，具体可下载demo

#### 本项目小说内容抓取自天籁小说网、笔趣阁，仅限于学习使用，未经本人允许，不得挪作其他用途，否则后果自负

demo v3.0.8 下载体验： 链接: https://pan.baidu.com/s/1x22wuLF8P6dEY7C3F_l_YA 提取码: fbfq

v1.6.0 新增自动滚屏

v1.7.0 新增书籍缓存，缓存后可离线阅读

v1.7.1 修复章节过多造成的UI界面卡顿问题

v1.7.2 优化章节数巨大（>1000）的小说的加载速度

  修复对章节数巨大（>1000）的小说进行缓存操作时，程序会挂掉的问题

v1.8.0 新增书架整理功能（书架长按任意一本书进入编辑状态，可进行删除和位置拖放等操作）

v1.8.1 移除书架下拉刷新，改为自动刷新；

  修复由于书架多于一页上下滑动时与下拉刷新冲突的问题

v1.8.2 修复断网后离线阅读小说内容加载不出来的的问题

v2.0.0 更新搜索源，解决由于抓取的网站更新导致的相关问题，修复一系列容易导致崩溃bug

v2.0.2 书架UI错位修复以及搜索优化，修复部分bug，以及针对Android X 进行兼容性修改

v3.0.0 新增书城模块，主要内容为分类排行； 新增书城小说源，更多小说内容； 更新html抓取方式，优化小说内容抓取速度；更新搜索关键词，优化搜索方式；（ps:由于组件升级，可能老版本覆盖升级会导致阅读背景颜色异常，重新切换一下背景即可）

v3.0.1 修复书城因为快速滚动而产生的加载混乱等问题，优化书城加载平滑度。

v3.0.2 修复由于抓取网站https证书过期或使用自签名证书所导致的一系列问题  

v3.0.3 修复由于小说搜索源参数以及该页面html数据结构改动而导致应用内书源搜索总是结果为空的问题

v3.0.5 更新书城抓取页面连接，解决书城可能无法加载的问题

v3.0.6 修复由于抓取网站的结构性变化，而导致的书城栏目小说列表无法加载的问题

v3.0.7 更新小说搜索模块，新增笔趣阁搜索源，实现多源搜索

v3.0.8 更新书源域名，修复小说加载失败的问题，修复小说搜索概率性搜索结果缺失的问题

v3.0.9 更新书源域名，修复小说加载失败、书城加载不出来等问题
（旧版本覆盖更新后，书架中存在加载不出来的书或者不更新章节的书，请删掉，重新加入书架即可恢复正常）


![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/1.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/2.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/3.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/4.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/5.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/6.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/7.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/8.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/9.png)
![demoImg](https://raw.githubusercontent.com/MissZzz1/MissZzzReader/master/img/10.png)



## License
```
   Copyright 2018 MissZzz1, All right reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```