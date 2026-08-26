import * as matchers from "@testing-library/jest-dom";
import { expect, afterEach } from "vitest";
import { cleanup } from "@testing-library/react";
// make sure that expect has all the react testing library matchers
expect.extend(matchers);
// wipe the DOM after each test
afterEach(() => {
  cleanup();
});
