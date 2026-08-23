import classes from "./Header.module.scss";

export default function Header() {
  const today = new Date();

  const formatted = today.toLocaleDateString("en-US", {
    weekday: "long",
    month: "long",
    day: "numeric",
  });

  return (
    <header className={classes.header}>
      <h1 className={classes.header__title}>Today's Tasks</h1>
      <p className={classes.header__date}>{formatted}</p>
    </header>
  );
}
