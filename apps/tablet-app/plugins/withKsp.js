/* eslint-disable @typescript-eslint/no-require-imports */
const { withProjectBuildGradle } = require('@expo/config-plugins');

module.exports = function withKsp(expoConfig) {
  return withProjectBuildGradle(expoConfig, (config) => {
    const contents = config.modResults.contents;

    if (!config.modResults.contents.includes('plugins')) {
      const buildscriptEnd = contents.indexOf('\n}', contents.indexOf('buildscript {'));
      const insertPosition = buildscriptEnd + 2;

      const kspPlugin = `\n\nplugins {\n}`;

      config.modResults.contents =
        contents.slice(0, insertPosition) +
        kspPlugin +
        contents.slice(insertPosition);
    }

    config.modResults.contents = config.modResults.contents.replace(
      /plugins \{/,
      `plugins {\n  id 'com.google.devtools.ksp' version "2.1.20-1.0.32" apply false`
    );

    return config;
  });
};
