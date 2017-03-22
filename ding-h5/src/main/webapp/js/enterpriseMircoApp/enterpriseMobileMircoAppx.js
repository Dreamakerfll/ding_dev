const Grid = SaltUI.Grid;
const Icon = SaltUI.Icon;
const Avatar = SaltUI.Avatar;

class UserInfoFiled extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            user:userInfo
        };
        console.log(1234);
        console.log(userInfo);
        console.log(userInfo.avatar);
        
    }
    componentDidMount() {
        this.timer = setTimeout(
          () => { console.log(userInfo); this.setState({user: userInfo}); },
          2000
        );
     }
    
    componentWillUnmount() {
        clearTimeout(this.timer)
      }
    
    render(){
        return (<div className="top_info">
        <div className="user_detail">
            <Avatar name={this.state.user.name} src={this.state.user.avatar} size="30px"/>
            <div className="user_name">{this.state.user.name}{this.state.user.userid}</div>
        </div>
        </div>);
    }
}

ReactDOM.render(
    <UserInfoFiled/>,
    document.getElementById('top_info')
);


class LogoFiled extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
        };
    }
    
    render(){
        return (<img src="http://dev.ijf.jianfa.cn/ijf/v-1.6.24-zh_CN-/ijianfa/img/banner.jpg" className="logo"/>);
    }
}

ReactDOM.render(
    <LogoFiled/>,
    document.getElementById('logo_ijf')
);




class GridField extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
        }
    }
    
    
    
    abcClick(e){
    	console.log(e);
    	openContant();
    	e.stopPropagation();
    	e.nativeEvent.stopImmediatePropagation();
    }

    getUser(){
    	getUser();
    }
    
    createChat(){
    	createChat();
    }
    
    selectDepartment(){
    	selectDepartment();
    }
    
    openMement(){
    	openMement();
    }
    render() {
        return (
            <Grid col={3} className="t-BCf" square={true} touchable={true}>
                <div className="demo"  onClick={this.abcClick}>
                    <Icon name="setting" fill={'#42A5F5'}/>
                    <div className="menu-title">企业通讯录</div>
                </div>
                <div className="demo" onClick={this.getUser}>
                    <Icon name="time" fill={'#FF8A65'}/>
                    <div className="menu-title">获取当前登录者信息</div>
                </div>
                <div className="demo">
                    <Icon name="star" fill={'#EA80FC'} onClick={this.selectDepartment}/>
                    <div className="menu-title">选择部门</div>
                </div>
                <div className="demo">
                    <Icon name="map" fill={'#EF9A9A'} onClick={this.createChat}/>
                    <div className="menu-title">创建聊天</div>
                </div>
                <div className="demo">
                    <Icon name="pen" fill={'#9FA8DA'} onClick={this.openMement}/>
                    <div className="menu-title">选择部门，人员</div>
                </div>
                <div className="demo">
                    <Icon name="info-circle" fill={'#80DEEA'}/>
                    <div className="menu-title">信息</div>
                </div>
                <div className="demo">
                    <Icon name="plus-circle" fill={'#DCE775'}/>
                    <div className="menu-title">添加</div>
                </div>
                <div className="demo">
                    <Icon name="search" fill={'#A1887F'}/>
                    <div className="menu-title">搜索</div>
                </div>
                <div className="demo">
                    <Icon name="plus" fill={'#BDBDBD'}/>
                    <div className="menu-title" style={{color: '#bbb'}}>添加</div>
                </div>
            </Grid>
        );
    }
};

ReactDOM.render(
    <GridField />,
    document.getElementById('content_grid')
);