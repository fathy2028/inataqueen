#!/bin/sh

# Replace BACKEND_URL in nginx config template
envsubst '${BACKEND_URL}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

# Inject the backend URL into the React app at runtime
# This creates a config file the React app reads at startup
cat > /usr/share/nginx/html/config.js << EOF
window.__RUNTIME_CONFIG__ = {
  BACKEND_URL: "${BACKEND_URL}"
};
EOF

exec "$@"
