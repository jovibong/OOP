import { TestimonialsColumn } from "../components/TestimonialsColumn";
import { motion } from "motion/react";

const testimonials = [
  {
    text: "SingHealth made booking appointments so simple! The online system is intuitive and saved me hours of waiting on the phone.",
    image:
      "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
    name: "Sarah Lim",
    role: "Patient",
  },
  {
    text: "The staff at SingHealth are incredibly professional and caring. They made my treatment journey comfortable and stress-free.",
    image:
      "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
    name: "David Tan",
    role: "Patient",
  },
  {
    text: "I love how easy it is to reschedule appointments and view my medical records online. The platform is modern and user-friendly.",
    image:
      "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop",
    name: "Michelle Wong",
    role: "Patient",
  },
  {
    text: "Excellent healthcare services with minimal wait times. The queue system keeps me informed and I never have to wait long.",
    image:
      "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&h=100&fit=crop",
    name: "James Chen",
    role: "Patient",
  },
  {
    text: "The doctors at SingHealth are highly skilled and take time to explain everything clearly. I feel confident in their care.",
    image:
      "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=100&h=100&fit=crop",
    name: "Emily Koh",
    role: "Patient",
  },
  {
    text: "Being able to book specialist appointments online is a game-changer. The whole experience has been seamless and efficient.",
    image:
      "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=100&h=100&fit=crop",
    name: "Ryan Lee",
    role: "Patient",
  },
  {
    text: "The cardiology department provided exceptional care during my treatment. The facilities are top-notch and the staff are amazing.",
    image:
      "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=100&h=100&fit=crop",
    name: "Amanda Ng",
    role: "Patient",
  },
  {
    text: "SingHealth's appointment system is so convenient! I can manage all my family's healthcare needs from one place.",
    image:
      "https://images.unsplash.com/photo-1519345182560-3f2917c472ef?w=100&h=100&fit=crop",
    name: "Marcus Ong",
    role: "Patient",
  },
  {
    text: "The medical summaries are detailed yet easy to understand. I appreciate the transparency and thorough communication.",
    image:
      "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&h=100&fit=crop",
    name: "Rachel Teo",
    role: "Patient",
  },
];

const firstColumn = testimonials.slice(0, 3);
const secondColumn = testimonials.slice(3, 6);
const thirdColumn = testimonials.slice(6, 9);

const Testimonials = () => {
  return (
    <section className="bg-white py-5 position-relative">
      <div className="container py-5">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.1, ease: [0.16, 1, 0.3, 1] }}
          viewport={{ once: true }}
          className="d-flex flex-column align-items-center justify-content-center mx-auto"
          style={{ maxWidth: "540px" }}
        >
          <div className="d-flex justify-content-center">
            <div
              className="border rounded-pill py-1 px-4 bg-light"
              style={{ display: "inline-block" }}
            >
              Testimonials
            </div>
          </div>

          <h2 className="text-center fw-bold mt-4 display-5">
            What our patients say
          </h2>
          <p className="text-center mt-3 text-muted">
            See what our patients have to say about their SingHealth experience.
          </p>
        </motion.div>

        <div
          className="d-flex justify-content-center gap-3 mt-5 overflow-hidden"
          style={{
            gap: "1.5rem",
            maxHeight: "740px",
            maskImage:
              "linear-gradient(to bottom, transparent, black 25%, black 75%, transparent)",
            WebkitMaskImage:
              "linear-gradient(to bottom, transparent, black 25%, black 75%, transparent)",
          }}
        >
          <TestimonialsColumn testimonials={firstColumn} duration={15} />
          <TestimonialsColumn
            testimonials={secondColumn}
            className="d-none d-md-block"
            duration={19}
          />
          <TestimonialsColumn
            testimonials={thirdColumn}
            className="d-none d-lg-block"
            duration={17}
          />
        </div>
      </div>
    </section>
  );
};

export default Testimonials;
