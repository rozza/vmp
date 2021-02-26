import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
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
import Tooltip from '@material-ui/core/Tooltip';
import {Route, HashRouter, Switch, Link} from 'react-router-dom';

import { defaultStyles } from './components/styles';
import Loader from './components/Loader';
import GraphQLProvider from './components/graphql/GraphQLProvider';
import Navigation from './components/Navigation';
import Copyright from './components/Copyright';

const Home = React.lazy(() => import('./components/Home'));
const GraphiQL = React.lazy(() => import('./components/GraphiQL'));
const Swagger = React.lazy(() => import('./components/Swagger'));
const MongoCollections = React.lazy(() => import( './components/graphql/MongoCollections'));
const MongoCollection = React.lazy(() => import( './components/graphql/MongoCollection'));

function IndexPage() {
  const classes = defaultStyles();
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
          <Tooltip title="There are no bugs only possibilities" arrow>
          <IconButton color="inherit">
            <Badge badgeContent={-1} color="secondary">
              <BugReportIcon />
            </Badge>
          </IconButton>
          </Tooltip>
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
        <Container maxWidth={false} className={classes.container}>
          <div>
          <GraphQLProvider>
          <Suspense fallback={<Loader />}>
            <Switch>
              <Route exact path="/" component={Home} />
              <Route path="/gui" component={GraphiQL} />
              <Route path="/sui" component={Swagger} />
              <Route path="/collections" component={MongoCollections} />
              <Route path="/collection/:databaseName/:collectionName" component={MongoCollection} />
            </Switch>
          </Suspense>
          </GraphQLProvider>
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

ReactDOM.render(
  <IndexPage />,
  document.querySelector('#root'),
);
