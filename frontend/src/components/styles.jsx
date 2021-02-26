import React from 'react';
import { makeStyles } from '@material-ui/core/styles';

const drawerWidth = 240;

export const styles = (theme) => ({
   root: {
     display: 'flex',
     flex: 1
   },
   toolbar: {
     paddingRight: 24, // keep right padding when drawer closed
   },
   toolbarIcon: {
     display: 'flex',
     alignItems: 'center',
     justifyContent: 'flex-end',
     padding: '0 8px',
     ...theme.mixins.toolbar,
   },
   appBar: {
     zIndex: theme.zIndex.drawer + 1,
     transition: theme.transitions.create(['width', 'margin'], {
       easing: theme.transitions.easing.sharp,
       duration: theme.transitions.duration.leavingScreen,
     }),
   },
   appBarShift: {
     marginLeft: drawerWidth,
     width: `calc(100% - ${drawerWidth}px)`,
     transition: theme.transitions.create(['width', 'margin'], {
       easing: theme.transitions.easing.sharp,
       duration: theme.transitions.duration.enteringScreen,
     }),
   },
   menuButton: {
     marginRight: 36,
   },
   menuButtonHidden: {
     display: 'none',
   },
   title: {
     flexGrow: 1,
   },
   drawerPaper: {
     position: 'relative',
     whiteSpace: 'nowrap',
     width: drawerWidth,
     transition: theme.transitions.create('width', {
       easing: theme.transitions.easing.sharp,
       duration: theme.transitions.duration.enteringScreen,
     }),
   },
   drawerPaperClose: {
     overflowX: 'hidden',
     transition: theme.transitions.create('width', {
       easing: theme.transitions.easing.sharp,
       duration: theme.transitions.duration.leavingScreen,
     }),
     width: theme.spacing(7),
     [theme.breakpoints.up('sm')]: {
       width: theme.spacing(9),
     },
   },
   appBarSpacer: theme.mixins.toolbar,
   content: {
     flexGrow: 1,
     height: '100vh',
     overflow: 'auto',
   },
   container: {
     paddingTop: theme.spacing(1),
     paddingBottom: theme.spacing(10),
   },
   paper: {
     padding: theme.spacing(2),
     display: 'flex',
     overflow: 'auto',
     flexDirection: 'column',
   },
   homePaper: {
       margin: `${theme.spacing(3)}px auto`,
       padding: theme.spacing(2),
     },
   avatarGridItem: {
     padding: theme.spacing(4),
   },
   largeAvatar: {
      marginLeft: theme.spacing(20),
       width: theme.spacing(300),
       height: theme.spacing(10),
   },
   fixedHeight: {
     height: 340,
   },
   padded: {
     padding: theme.spacing(4),
   },
});

export const defaultStyles = makeStyles(styles);
