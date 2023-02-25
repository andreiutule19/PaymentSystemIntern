import React from 'react';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';
import * as IoIcons from 'react-icons/io';
import * as RiIcons from 'react-icons/ri';
import * as ImIcons from 'react-icons/im';
import * as GiIcons from 'react-icons/gi';
import * as BiIcons from 'react-icons/bi';

export const SidebarData = [
  {
    title: 'Home alone',
    path: '/home',
    icon: <AiIcons.AiFillHome />
  },

  {
    title: 'Manage users',
    path: '',
    icon: <IoIcons.IoMdPeople />,
    iconClosed: <RiIcons.RiArrowDownSFill />,
    iconOpened: <RiIcons.RiArrowUpSFill />,

    subNav: [
      {
        title: 'Change password',
        path: '/changePass',
        icon: <IoIcons.IoIosKey />
      },
      {
        title: 'List users',
        path: '/usersList',
        icon: <IoIcons.IoIosListBox />
      },
      
      {
        title: 'Create user',
        path: '/register',
        icon: <IoIcons.IoIosCloudUpload />
      },
      {
        title: 'Approve',
        path: '/approve',
        icon: <IoIcons.IoIosCheckmarkCircle />
      }
      
    ]
  },

  {
    title: 'Accountancy',
    path: '',
    icon: <FaIcons.FaMoneyCheckAlt />,
    iconClosed: <RiIcons.RiArrowDownSFill />,
    iconOpened: <RiIcons.RiArrowUpSFill />,

    subNav: [
      {
        title: 'Balances',
        path: '/balances',
        icon: <ImIcons.ImStatsDots />
      },
      {
        title: 'List accounts',
        path: '/accountsList',
        icon: <GiIcons.GiMoneyStack />
      },
      
      {
        title: 'Create account',
        path: '/createAccount',
        icon: <IoIcons.IoIosCloudUpload />
      },
      {
        title: 'Approve',
        path: '/approveAccount',
        icon: <IoIcons.IoIosCheckmarkCircle />
      }
      
    ]
  },

  {
    title: 'Payments',
    path: '',
    icon: <BiIcons.BiCoin />,
    iconClosed: <RiIcons.RiArrowDownSFill />,
    iconOpened: <RiIcons.RiArrowUpSFill />,

    subNav: [
     
      {
        title: 'Approve payments',
        path: '/payList',
        icon: <GiIcons.GiMoneyStack />
      },
      
      {
        title: 'Make payment',
        path: '/pay',
        icon: <IoIcons.IoIosCloudUpload />
      },

      {
        title: 'List All',
        path: '/payments',
        icon: <IoIcons.IoIosListBox />
      },
      
    ]
  },
         
  {
    title: 'Support',
    path: '',
    icon: <IoIcons.IoMdHelpCircle />
  },
  {
    title: 'Log Out',
    path: '/',
    icon: <IoIcons.IoMdLogOut />,
  
  }
];