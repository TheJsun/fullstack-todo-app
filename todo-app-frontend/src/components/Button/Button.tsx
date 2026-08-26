import classes from "./Button.module.scss";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "primary" | "delete" | "edit";
}

export default function Button({
  variant = "primary",
  className = "",
  type = "button",
  onClick,
  children,
}: ButtonProps) {
  return (
    <button
      className={`${classes.btn} ${classes[className]} ${classes[`btn--${variant}`]}`}
      type={type}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
