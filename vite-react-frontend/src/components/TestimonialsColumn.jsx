import React from "react";
import { motion } from "motion/react";

export const TestimonialsColumn = ({
  className = "",
  testimonials,
  duration = 10,
}) => {
  return (
    <div className={className}>
      <motion.div
        animate={{
          translateY: "-50%",
        }}
        transition={{
          duration: duration,
          repeat: Infinity,
          ease: "linear",
          repeatType: "loop",
        }}
        className="d-flex flex-column gap-3 pb-3"
        style={{ gap: "1.5rem", paddingBottom: "1.5rem" }}
      >
        {[
          ...new Array(2).fill(0).map((_, index) => (
            <React.Fragment key={index}>
              {testimonials.map(({ text, image, name, role }, i) => (
                <div
                  className="card shadow-lg border-0"
                  style={{
                    maxWidth: "320px",
                    width: "100%",
                    borderRadius: "1.5rem",
                    boxShadow: "0 10px 30px rgba(13, 110, 253, 0.1)",
                  }}
                  key={i}
                >
                  <div className="card-body p-4">
                    <p className="card-text mb-3">{text}</p>
                    <div className="d-flex align-items-center gap-2 mt-3">
                      <img
                        width={40}
                        height={40}
                        src={image}
                        alt={name}
                        className="rounded-circle"
                        style={{
                          width: "40px",
                          height: "40px",
                          objectFit: "cover",
                        }}
                      />
                      <div className="d-flex flex-column">
                        <div
                          className="fw-medium"
                          style={{ lineHeight: "1.25" }}
                        >
                          {name}
                        </div>
                        <div
                          className="text-muted small"
                          style={{ lineHeight: "1.25", opacity: 0.7 }}
                        >
                          {role}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </React.Fragment>
          )),
        ]}
      </motion.div>
    </div>
  );
};
