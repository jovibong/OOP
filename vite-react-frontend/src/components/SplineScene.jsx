import { Suspense, lazy } from "react";

// Lazy load Spline for better performance
const Spline = lazy(() => import("@splinetool/react-spline"));

/**
 * SplineScene - A wrapper component for rendering 3D Spline scenes
 * @param {string} scene - URL to the Spline scene
 * @param {string} className - Optional CSS classes
 */
export function SplineScene({ scene, className = "" }) {
  return (
    <Suspense
      fallback={
        <div className="w-100 h-100 d-flex align-items-center justify-content-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading 3D scene...</span>
          </div>
        </div>
      }
    >
      <Spline scene={scene} className={className} />
    </Suspense>
  );
}

export default SplineScene;
