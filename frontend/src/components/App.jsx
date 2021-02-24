import React from 'react';
import clsx from 'clsx';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import Box from '@material-ui/core/Box';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import Container from '@material-ui/core/Container';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import BugReportIcon from '@material-ui/icons/BugReport';
import { useStyles } from './styles';
import {Route, HashRouter, Switch, Link} from 'react-router-dom';


import Navigation from './Navigation';
import Home from './Home';
import GraphiQL from './GraphiQL';
import Forms from './Forms';
import Copyright from './Copyright';

export default function App() {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const handleDrawerOpen = () => {
    setOpen(true);
  };
  const handleDrawerClose = () => {
    setOpen(false);
  };
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
        <Toolbar className={classes.toolbar}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
          >
            <MenuIcon />
          </IconButton>
          <Typography component="h1" variant="h4" color="inherit" noWrap className={classes.title}>
            Visual MongoDB Pro<Typography component="span" variant="h6" color="inherit" noWrap className={classes.title}>(totype)
            </Typography>
          </Typography>
          <IconButton color="inherit">
            <Badge badgeContent={-1} color="secondary">
              <BugReportIcon />
            </Badge>
          </IconButton>
        </Toolbar>
      </AppBar>
      <HashRouter>
      <Drawer
        variant="permanent"
        classes={{
          paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
        }}
        open={open}
      >
        <div className={classes.toolbarIcon}>
          <IconButton onClick={handleDrawerClose}>
            <ChevronLeftIcon />
          </IconButton>
        </div>
        <Divider />
        <Navigation />
        <Divider />
      </Drawer>
      <main className={classes.content}>
        <div className={classes.appBarSpacer} />
        <Container maxWidth="false" className={classes.container}>
          <div>
            <Switch>
              <Route exact path="/" component={Home} />
              <Route path="/gui" component={GraphiQL} />
              <Route path="/forms" component={Forms} />
            </Switch>
          </div>
          <Box pt={4}>
            <Copyright />
          </Box>
        </Container>
      </main>
      </HashRouter>
    </div>
  );
}
