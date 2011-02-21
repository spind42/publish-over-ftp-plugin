/*
 * The MIT License
 *
 * Copyright (C) 2010-2011 by Anthony Robinson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jenkins.plugins.publish_over_ftp;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.plugins.publish_over.BPInstanceConfig;
import jenkins.plugins.publish_over.BPPlugin;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;

public class BapFtpPromotionPublisherPlugin extends Notifier {

    @Extension
    public static final Descriptor DESCRIPTOR = new Descriptor();

    private BapFtpPublisherPlugin delegate;

    @DataBoundConstructor
    public BapFtpPromotionPublisherPlugin(final List<BapFtpPublisher> publishers, final boolean continueOnError, final boolean failOnError,
                                          final boolean alwaysPublishFromMaster, final String masterNodeName) {
        this.delegate = new BapFtpPublisherPlugin(publishers, continueOnError, failOnError, alwaysPublishFromMaster, masterNodeName);
    }

    public BPInstanceConfig getInstanceConfig() {
        return delegate.getInstanceConfig();
    }

    @Override
    public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener)
                    throws InterruptedException, IOException {
        return delegate.perform(build, launcher, listener);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return delegate.getRequiredMonitorService();
    }

    protected HashCodeBuilder createHashCodeBuilder() {
        return addToHashCode(new HashCodeBuilder());
    }

    protected HashCodeBuilder addToHashCode(final HashCodeBuilder builder) {
        return builder.append(delegate);
    }

    protected EqualsBuilder createEqualsBuilder(final BapFtpPromotionPublisherPlugin that) {
        return addToEquals(new EqualsBuilder(), that);
    }

    protected EqualsBuilder addToEquals(final EqualsBuilder builder, final BapFtpPromotionPublisherPlugin that) {
        return builder.append(delegate, that.delegate);
    }

    protected ToStringBuilder addToToString(final ToStringBuilder builder) {
        return builder.append("delegate", delegate);
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        return createEqualsBuilder((BapFtpPromotionPublisherPlugin) o).isEquals();
    }

    public int hashCode() {
        return createHashCodeBuilder().toHashCode();
    }

    public String toString() {
        return addToToString(new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

    public static class Descriptor extends BuildStepDescriptor<Publisher> {
        public boolean isApplicable(final Class<? extends AbstractProject> aClass) {
            return BPPlugin.PROMOTION_JOB_TYPE.equals(aClass.getCanonicalName());
        }
        public String getDisplayName() {
            return Messages.promotion_descriptor_displayName();
        }
        public String getConfigPage() {
            return getViewPage(BapFtpPublisherPlugin.class, "config.jelly");
        }
        public BapFtpPublisherPlugin.Descriptor getPublisherDescriptor() {
            return BapFtpPublisherPlugin.DESCRIPTOR;
        }
    }

}
