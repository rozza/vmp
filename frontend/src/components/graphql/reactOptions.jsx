// Any GraphQL API can be queried in components, where fetch options for the
// URI, auth headers, etc. can be specified. The `useGraphQL` hook will do less
// work for following renders if `fetchOptionsOverride` is defined outside the
// component, or is memoized using the `React.useMemo` hook within the
// component. Typically it’s exported in a config module for use throughout the
// project. The default fetch options received by the override function are
// tailored to the operation; usually the body is JSON but if there are files in
// the variables it will be a `FormData` instance for a GraphQL multipart
// request.
export function reactOptions(options) {
  options.url = 'http://localhost:8080/graphql';
}


