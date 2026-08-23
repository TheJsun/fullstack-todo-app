import classes from "./Header.module.scss";

export default function Header() {
  const today = new Date();

  const formatted = today.toLocaleDateString("en-US", {
    weekday: "long",
    month: "long",
    day: "numeric",
  });

  return (
    <main className={classes.title}>
      <h1>Today's Tasks</h1>
      <p>{formatted}</p>
    </main>
  );
}
