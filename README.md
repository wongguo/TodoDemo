# TodoDemo
ITAEM 2022团队招新二轮项目

### **账号模块**

- 账号的注册登录
- 修改用户名
    - 修改成功：用户名显示更新
    - 修改失败：Toast提示
- 修改密码
    - 修改成功：返回登录界面重新登录
    - 修改失败：Toast提示
- 注销账号
    - 注销成功：退出登录状态，对应账号下的Todo清空
    - 注销失败：Toast提示

### Todo模块

- Todo的显示
    - 未完成Todo列表
    - 完成Todo列表
    - 选择对应分类的Todo（默认是显示全部Todo）
- Todo的新建
    - 标题（必填）
    - 分类（选填）
    - 时间（选填）：默认值为创建当天的时间
- Todo的操作
    - 点击
        - 编辑模式下：显示编辑Todo功能
        - 多选模式下：更改Todo背景颜色表示选中对应的Todo
    - 长按：打开PopupMenu
        - 删除该项：数据库清除该条内容
        - 更多：进入多选模式
            - 选中的Todo背景色加深，再去点击取消选中背景色回复正常
            - 删除按钮显示：点击删除选中Todo
            - 添加按钮变为取消按钮：退出多选模式
- 搜索功能
    - 模糊搜索：显示对应账号下含有与搜索内容的相关标题的Todo
    - 搜索显示的Todo
        - 点击：显示编辑Todo功能
        - 长按：选中是否删除该Todo
