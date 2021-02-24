import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import ListAltIcon from '@material-ui/icons/ListAlt';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';

import { Link, Router, BrowserRouter} from 'react-router-dom';

export default function Navigation() {
  return (
  <div>
  <List>
    <ListItem button>
      <ListItemIcon>
        <DashboardIcon />
      </ListItemIcon>
       <Link to="/"><ListItemText primary="Home" /></Link>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <TrendingUpIcon />
      </ListItemIcon>
      <Link to="/gui"><ListItemText primary="GraphiQL UI" /></Link>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <ListAltIcon />
      </ListItemIcon>
      <Link to="/forms"><ListItemText primary="Forms" /></Link>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <BarChartIcon />
      </ListItemIcon>
      <ListItemText primary="Reports" secondary="(Coming soon)"/>
    </ListItem>
    </List>
  </div>
);
}
