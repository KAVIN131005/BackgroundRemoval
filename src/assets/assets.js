import logo from'./logo.png';
import video_banner from './home-page-video_banner.mp4';
import people from './people.png';
import people_org from './people-org.png';
import credits from './credits.png';
export const assets = {
    logo,
    video_banner,
    people,
    people_org,
    credits,
}
export const steps = [
  {
    step: "Step 1",
    title: "Select the Image",
    description: `Choose the photo you want to remove the background from. You can upload it from your device or paste an image URL.`
       
        
       
,
  },
  {
    step: "Step 2",
    title: "Let the Magic Remove the Background",
    description: `Our AI-powered tool will automatically detect and remove the background from your image instantly.`,
  },
  {
    step: "Step 3",
    title: "Download Your Image",
    description: `Download your new image with a transparent background or add a new background of your choice.`,
  },
];
export const categories=["people","Product","Animals","Cars","Graphics"];


export const plans = [
  {
    id: "Basic",
    name: "Basic Package",
    price: 499,
    credits: "100 credits",
    description: "Best for personal use",
    popular: false
  },
  {
    id: "Premium",
    name: "Premium Package",
    price: 899,
    credits: "250 credits",
    description: "Best for business use",
    popular: true
  },
  {
    id: "Ultimate",
    name: "Ultimate Package",
    price: 1499,
    credits: "1000 credits",
    description: "Best for enterprise use",
    popular: false
  }
];

export const testimonials = [
  {
    id: 1,
    quote: "We are impressed by the AI and think it's the best choice on the market.",
    author: "Anthony Walker",
    handle: "@_webarchitect_"
  },
  {
    id: 2,
    quote: "remove.bg is leaps and bounds ahead of the competition. A thousand times better. It simplified the whole process.",
    author: "Sarah Johnson",
    handle: "@techlead_sarah"
  },
  {
    id: 3,
    quote: "We were impressed by its ability to account for pesky, feathery hair without making an image look jagged and amateurish.",
    author: "Michael Chen",
    handle: "@coding_newbie"
  }
];

export const FOOTER_CONSTANTS = [
  {
    url: "https://facebook.com",
    logo: "https://img.icons8.com/fluent/30/000000/facebook-new.png"
  },
  {
    url: "https://linkedin.com",
    logo: "https://img.icons8.com/fluent/30/000000/linkedin-2.png"
  },
  {
    url: "https://instagram.com",
    logo: "https://img.icons8.com/fluent/30/000000/instagram-new.png"
  },
  {
    url: "https://twitter.com",
    logo: "https://img.icons8.com/fluent/30/000000/twitter.png"
  }
];
