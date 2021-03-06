/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.visual.colorscale.imp;

import javax.vecmath.Color3f;

import seeit3d.internal.base.visual.colorscale.IColorScale;

/**
 * Implementation of Rainbow color scale. Based on scale provided in http://www.cs.uml.edu/~haim/ColorCenter/Programs/ColorScales/ColorScales.html
 * 
 * @author David Montaño
 * 
 */
public class Rainbow implements IColorScale {

	private static Color3f[] colors;

	public Rainbow() {
		colors = new Color3f[256];
		colors[0] = new Color3f(0.0f, 0.0f, 0.0f);
		colors[1] = new Color3f(0.1764706f, 0.0f, 0.14117648f);
		colors[2] = new Color3f(0.21960784f, 0.0f, 0.18039216f);
		colors[3] = new Color3f(0.23529412f, 0.0f, 0.19215687f);
		colors[4] = new Color3f(0.2627451f, 0.0f, 0.21176471f);
		colors[5] = new Color3f(0.27450982f, 0.0f, 0.23137255f);
		colors[6] = new Color3f(0.2784314f, 0.0f, 0.23921569f);
		colors[7] = new Color3f(0.29411766f, 0.0f, 0.26666668f);
		colors[8] = new Color3f(0.2901961f, 0.0f, 0.28627452f);
		colors[9] = new Color3f(0.2901961f, 0.0f, 0.3019608f);
		colors[10] = new Color3f(0.28627452f, 0.0f, 0.31764707f);
		colors[11] = new Color3f(0.2784314f, 0.0f, 0.34117648f);
		colors[12] = new Color3f(0.27058825f, 0.003921569f, 0.3529412f);
		colors[13] = new Color3f(0.26666668f, 0.007843138f, 0.36862746f);
		colors[14] = new Color3f(0.25882354f, 0.011764706f, 0.38039216f);
		colors[15] = new Color3f(0.24705882f, 0.023529412f, 0.4f);
		colors[16] = new Color3f(0.23921569f, 0.02745098f, 0.41568628f);
		colors[17] = new Color3f(0.22745098f, 0.039215688f, 0.42745098f);
		colors[18] = new Color3f(0.21960784f, 0.047058824f, 0.44313726f);
		colors[19] = new Color3f(0.20784314f, 0.05882353f, 0.45490196f);
		colors[20] = new Color3f(0.1882353f, 0.07058824f, 0.46666667f);
		colors[21] = new Color3f(0.18431373f, 0.078431375f, 0.4745098f);
		colors[22] = new Color3f(0.17254902f, 0.09019608f, 0.4862745f);
		colors[23] = new Color3f(0.16078432f, 0.105882354f, 0.5019608f);
		colors[24] = new Color3f(0.15686275f, 0.10980392f, 0.5058824f);
		colors[25] = new Color3f(0.14509805f, 0.1254902f, 0.5176471f);
		colors[26] = new Color3f(0.13333334f, 0.14117648f, 0.5254902f);
		colors[27] = new Color3f(0.11372549f, 0.16862746f, 0.5372549f);
		colors[28] = new Color3f(0.09803922f, 0.20392157f, 0.5411765f);
		colors[29] = new Color3f(0.09411765f, 0.22352941f, 0.54509807f);
		colors[30] = new Color3f(0.09411765f, 0.24313726f, 0.5529412f);
		colors[31] = new Color3f(0.09411765f, 0.2509804f, 0.5568628f);
		colors[32] = new Color3f(0.09019608f, 0.25490198f, 0.5568628f);
		colors[33] = new Color3f(0.09019608f, 0.27058825f, 0.56078434f);
		colors[34] = new Color3f(0.09019608f, 0.2784314f, 0.5568628f);
		colors[35] = new Color3f(0.09019608f, 0.2784314f, 0.5568628f);
		colors[36] = new Color3f(0.09019608f, 0.28627452f, 0.5568628f);
		colors[37] = new Color3f(0.09019608f, 0.29411766f, 0.5568628f);
		colors[38] = new Color3f(0.09019608f, 0.29411766f, 0.5568628f);
		colors[39] = new Color3f(0.09019608f, 0.30588236f, 0.5568628f);
		colors[40] = new Color3f(0.09019608f, 0.3137255f, 0.5568628f);
		colors[41] = new Color3f(0.09019608f, 0.3137255f, 0.5568628f);
		colors[42] = new Color3f(0.09019608f, 0.32156864f, 0.5529412f);
		colors[43] = new Color3f(0.09019608f, 0.33333334f, 0.5529412f);
		colors[44] = new Color3f(0.09019608f, 0.33333334f, 0.5529412f);
		colors[45] = new Color3f(0.09019608f, 0.34117648f, 0.54901963f);
		colors[46] = new Color3f(0.09019608f, 0.34117648f, 0.54901963f);
		colors[47] = new Color3f(0.09411765f, 0.3529412f, 0.54901963f);
		colors[48] = new Color3f(0.09411765f, 0.3529412f, 0.54901963f);
		colors[49] = new Color3f(0.09411765f, 0.3647059f, 0.54509807f);
		colors[50] = new Color3f(0.09411765f, 0.3647059f, 0.54509807f);
		colors[51] = new Color3f(0.09411765f, 0.3647059f, 0.54509807f);
		colors[52] = new Color3f(0.09411765f, 0.3647059f, 0.54509807f);
		colors[53] = new Color3f(0.09411765f, 0.38039216f, 0.54509807f);
		colors[54] = new Color3f(0.09411765f, 0.38039216f, 0.54509807f);
		colors[55] = new Color3f(0.09803922f, 0.39607844f, 0.5411765f);
		colors[56] = new Color3f(0.09803922f, 0.39607844f, 0.5411765f);
		colors[57] = new Color3f(0.09803922f, 0.40784314f, 0.5372549f);
		colors[58] = new Color3f(0.09803922f, 0.40784314f, 0.5372549f);
		colors[59] = new Color3f(0.09803922f, 0.40784314f, 0.5372549f);
		colors[60] = new Color3f(0.101960786f, 0.42352942f, 0.5372549f);
		colors[61] = new Color3f(0.101960786f, 0.42352942f, 0.5372549f);
		colors[62] = new Color3f(0.105882354f, 0.43529412f, 0.53333336f);
		colors[63] = new Color3f(0.105882354f, 0.43529412f, 0.53333336f);
		colors[64] = new Color3f(0.105882354f, 0.43529412f, 0.53333336f);
		colors[65] = new Color3f(0.105882354f, 0.4509804f, 0.5294118f);
		colors[66] = new Color3f(0.105882354f, 0.4509804f, 0.5294118f);
		colors[67] = new Color3f(0.10980392f, 0.4627451f, 0.5254902f);
		colors[68] = new Color3f(0.10980392f, 0.4627451f, 0.5254902f);
		colors[69] = new Color3f(0.11372549f, 0.47843137f, 0.52156866f);
		colors[70] = new Color3f(0.11372549f, 0.47843137f, 0.52156866f);
		colors[71] = new Color3f(0.11372549f, 0.47843137f, 0.52156866f);
		colors[72] = new Color3f(0.11372549f, 0.47843137f, 0.52156866f);
		colors[73] = new Color3f(0.11372549f, 0.49019608f, 0.5176471f);
		colors[74] = new Color3f(0.11372549f, 0.49019608f, 0.5176471f);
		colors[75] = new Color3f(0.11764706f, 0.5019608f, 0.5137255f);
		colors[76] = new Color3f(0.11764706f, 0.5019608f, 0.5137255f);
		colors[77] = new Color3f(0.12156863f, 0.5137255f, 0.50980395f);
		colors[78] = new Color3f(0.12156863f, 0.5137255f, 0.50980395f);
		colors[79] = new Color3f(0.12156863f, 0.5137255f, 0.50980395f);
		colors[80] = new Color3f(0.1254902f, 0.5254902f, 0.5019608f);
		colors[81] = new Color3f(0.1254902f, 0.5254902f, 0.5019608f);
		colors[82] = new Color3f(0.12941177f, 0.5372549f, 0.49803922f);
		colors[83] = new Color3f(0.12941177f, 0.5372549f, 0.49803922f);
		colors[84] = new Color3f(0.12941177f, 0.5372549f, 0.49803922f);
		colors[85] = new Color3f(0.13333334f, 0.54901963f, 0.49019608f);
		colors[86] = new Color3f(0.13333334f, 0.54901963f, 0.49019608f);
		colors[87] = new Color3f(0.13725491f, 0.5568628f, 0.48235294f);
		colors[88] = new Color3f(0.13725491f, 0.5568628f, 0.48235294f);
		colors[89] = new Color3f(0.14117648f, 0.5686275f, 0.4745098f);
		colors[90] = new Color3f(0.14117648f, 0.5686275f, 0.4745098f);
		colors[91] = new Color3f(0.14117648f, 0.5686275f, 0.4745098f);
		colors[92] = new Color3f(0.14509805f, 0.5764706f, 0.4627451f);
		colors[93] = new Color3f(0.14509805f, 0.5764706f, 0.4627451f);
		colors[94] = new Color3f(0.14901961f, 0.5882353f, 0.45490196f);
		colors[95] = new Color3f(0.14901961f, 0.5882353f, 0.45490196f);
		colors[96] = new Color3f(0.15686275f, 0.59607846f, 0.44313726f);
		colors[97] = new Color3f(0.15686275f, 0.59607846f, 0.44313726f);
		colors[98] = new Color3f(0.16078432f, 0.6039216f, 0.43529412f);
		colors[99] = new Color3f(0.16078432f, 0.6039216f, 0.43529412f);
		colors[100] = new Color3f(0.16470589f, 0.6117647f, 0.42352942f);
		colors[101] = new Color3f(0.16470589f, 0.6117647f, 0.42352942f);
		colors[102] = new Color3f(0.16862746f, 0.61960787f, 0.41568628f);
		colors[103] = new Color3f(0.16862746f, 0.61960787f, 0.41568628f);
		colors[104] = new Color3f(0.16862746f, 0.61960787f, 0.41568628f);
		colors[105] = new Color3f(0.1764706f, 0.627451f, 0.40784314f);
		colors[106] = new Color3f(0.1764706f, 0.627451f, 0.40784314f);
		colors[107] = new Color3f(0.18039216f, 0.63529414f, 0.39607844f);
		colors[108] = new Color3f(0.18039216f, 0.63529414f, 0.39607844f);
		colors[109] = new Color3f(0.1882353f, 0.6431373f, 0.3882353f);
		colors[110] = new Color3f(0.1882353f, 0.6431373f, 0.3882353f);
		colors[111] = new Color3f(0.19607843f, 0.6509804f, 0.38039216f);
		colors[112] = new Color3f(0.19607843f, 0.6509804f, 0.38039216f);
		colors[113] = new Color3f(0.2f, 0.65882355f, 0.37254903f);
		colors[114] = new Color3f(0.20784314f, 0.6666667f, 0.3647059f);
		colors[115] = new Color3f(0.20784314f, 0.6666667f, 0.3647059f);
		colors[116] = new Color3f(0.20784314f, 0.6666667f, 0.3647059f);
		colors[117] = new Color3f(0.21568628f, 0.6745098f, 0.35686275f);
		colors[118] = new Color3f(0.21568628f, 0.6745098f, 0.35686275f);
		colors[119] = new Color3f(0.22352941f, 0.68235296f, 0.34509805f);
		colors[120] = new Color3f(0.22352941f, 0.68235296f, 0.34509805f);
		colors[121] = new Color3f(0.23137255f, 0.6862745f, 0.3372549f);
		colors[122] = new Color3f(0.24313726f, 0.69411767f, 0.32941177f);
		colors[123] = new Color3f(0.2509804f, 0.69803923f, 0.32156864f);
		colors[124] = new Color3f(0.2509804f, 0.69803923f, 0.32156864f);
		colors[125] = new Color3f(0.2627451f, 0.7058824f, 0.3137255f);
		colors[126] = new Color3f(0.2627451f, 0.7058824f, 0.3137255f);
		colors[127] = new Color3f(0.27058825f, 0.70980394f, 0.30980393f);
		colors[128] = new Color3f(0.28235295f, 0.7176471f, 0.3019608f);
		colors[129] = new Color3f(0.28235295f, 0.7176471f, 0.3019608f);
		colors[130] = new Color3f(0.28235295f, 0.7176471f, 0.3019608f);
		colors[131] = new Color3f(0.29411766f, 0.72156864f, 0.29803923f);
		colors[132] = new Color3f(0.3019608f, 0.7294118f, 0.2901961f);
		colors[133] = new Color3f(0.3137255f, 0.73333335f, 0.28627452f);
		colors[134] = new Color3f(0.3254902f, 0.7411765f, 0.28235295f);
		colors[135] = new Color3f(0.34117648f, 0.74509805f, 0.28235295f);
		colors[136] = new Color3f(0.35686275f, 0.7490196f, 0.2784314f);
		colors[137] = new Color3f(0.37254903f, 0.7529412f, 0.27450982f);
		colors[138] = new Color3f(0.3882353f, 0.75686276f, 0.27450982f);
		colors[139] = new Color3f(0.40392157f, 0.7607843f, 0.27450982f);
		colors[140] = new Color3f(0.41960785f, 0.7647059f, 0.27450982f);
		colors[141] = new Color3f(0.43529412f, 0.76862746f, 0.27450982f);
		colors[142] = new Color3f(0.43529412f, 0.76862746f, 0.27450982f);
		colors[143] = new Color3f(0.4509804f, 0.76862746f, 0.27450982f);
		colors[144] = new Color3f(0.46666667f, 0.77254903f, 0.27450982f);
		colors[145] = new Color3f(0.48235294f, 0.77254903f, 0.27450982f);
		colors[146] = new Color3f(0.50980395f, 0.7764706f, 0.2784314f);
		colors[147] = new Color3f(0.52156866f, 0.78039217f, 0.2784314f);
		colors[148] = new Color3f(0.5372549f, 0.78039217f, 0.28235295f);
		colors[149] = new Color3f(0.54901963f, 0.78039217f, 0.28235295f);
		colors[150] = new Color3f(0.56078434f, 0.78039217f, 0.28627452f);
		colors[151] = new Color3f(0.56078434f, 0.78039217f, 0.28627452f);
		colors[152] = new Color3f(0.5764706f, 0.78039217f, 0.28627452f);
		colors[153] = new Color3f(0.5882353f, 0.78039217f, 0.2901961f);
		colors[154] = new Color3f(0.6f, 0.78039217f, 0.2901961f);
		colors[155] = new Color3f(0.6117647f, 0.78039217f, 0.29411766f);
		colors[156] = new Color3f(0.627451f, 0.78431374f, 0.29803923f);
		colors[157] = new Color3f(0.654902f, 0.78431374f, 0.30588236f);
		colors[158] = new Color3f(0.6666667f, 0.78431374f, 0.30980393f);
		colors[159] = new Color3f(0.6784314f, 0.78431374f, 0.30980393f);
		colors[160] = new Color3f(0.6784314f, 0.78431374f, 0.30980393f);
		colors[161] = new Color3f(0.69411767f, 0.78431374f, 0.3137255f);
		colors[162] = new Color3f(0.7058824f, 0.78431374f, 0.31764707f);
		colors[163] = new Color3f(0.7176471f, 0.78039217f, 0.32156864f);
		colors[164] = new Color3f(0.7294118f, 0.78039217f, 0.32156864f);
		colors[165] = new Color3f(0.74509805f, 0.78039217f, 0.3254902f);
		colors[166] = new Color3f(0.76862746f, 0.78039217f, 0.33333334f);
		colors[167] = new Color3f(0.78039217f, 0.7764706f, 0.33333334f);
		colors[168] = new Color3f(0.78039217f, 0.7764706f, 0.33333334f);
		colors[169] = new Color3f(0.79607844f, 0.7764706f, 0.3372549f);
		colors[170] = new Color3f(0.80784315f, 0.77254903f, 0.34117648f);
		colors[171] = new Color3f(0.83137256f, 0.77254903f, 0.34901962f);
		colors[172] = new Color3f(0.84313726f, 0.76862746f, 0.3529412f);
		colors[173] = new Color3f(0.85490197f, 0.7647059f, 0.35686275f);
		colors[174] = new Color3f(0.8784314f, 0.7607843f, 0.36862746f);
		colors[175] = new Color3f(0.8784314f, 0.7607843f, 0.36862746f);
		colors[176] = new Color3f(0.9019608f, 0.75686276f, 0.3764706f);
		colors[177] = new Color3f(0.9137255f, 0.7529412f, 0.38431373f);
		colors[178] = new Color3f(0.9254902f, 0.74509805f, 0.39215687f);
		colors[179] = new Color3f(0.93333334f, 0.7411765f, 0.40784314f);
		colors[180] = new Color3f(0.9411765f, 0.7372549f, 0.41568628f);
		colors[181] = new Color3f(0.9411765f, 0.7372549f, 0.41568628f);
		colors[182] = new Color3f(0.9490196f, 0.73333335f, 0.43137255f);
		colors[183] = new Color3f(0.95686275f, 0.7254902f, 0.44705883f);
		colors[184] = new Color3f(0.9607843f, 0.72156864f, 0.45490196f);
		colors[185] = new Color3f(0.96862745f, 0.7176471f, 0.47058824f);
		colors[186] = new Color3f(0.972549f, 0.7137255f, 0.48235294f);
		colors[187] = new Color3f(0.972549f, 0.7137255f, 0.48235294f);
		colors[188] = new Color3f(0.98039216f, 0.70980394f, 0.49019608f);
		colors[189] = new Color3f(0.9843137f, 0.7058824f, 0.5019608f);
		colors[190] = new Color3f(0.9882353f, 0.7058824f, 0.50980395f);
		colors[191] = new Color3f(0.99215686f, 0.7058824f, 0.52156866f);
		colors[192] = new Color3f(0.99215686f, 0.7058824f, 0.52156866f);
		colors[193] = new Color3f(0.99607843f, 0.7058824f, 0.5254902f);
		colors[194] = new Color3f(0.99607843f, 0.7019608f, 0.5411765f);
		colors[195] = new Color3f(1.0f, 0.7019608f, 0.5568628f);
		colors[196] = new Color3f(1.0f, 0.7019608f, 0.5686275f);
		colors[197] = new Color3f(1.0f, 0.7019608f, 0.5686275f);
		colors[198] = new Color3f(1.0f, 0.7019608f, 0.59607846f);
		colors[199] = new Color3f(1.0f, 0.7058824f, 0.6313726f);
		colors[200] = new Color3f(1.0f, 0.7058824f, 0.6431373f);
		colors[201] = new Color3f(1.0f, 0.7058824f, 0.654902f);
		colors[202] = new Color3f(1.0f, 0.7058824f, 0.654902f);
		colors[203] = new Color3f(1.0f, 0.70980394f, 0.6627451f);
		colors[204] = new Color3f(1.0f, 0.70980394f, 0.6666667f);
		colors[205] = new Color3f(1.0f, 0.7137255f, 0.6784314f);
		colors[206] = new Color3f(1.0f, 0.7176471f, 0.6901961f);
		colors[207] = new Color3f(1.0f, 0.7176471f, 0.6901961f);
		colors[208] = new Color3f(1.0f, 0.72156864f, 0.7019608f);
		colors[209] = new Color3f(1.0f, 0.7254902f, 0.7019608f);
		colors[210] = new Color3f(1.0f, 0.7254902f, 0.7137255f);
		colors[211] = new Color3f(1.0f, 0.7294118f, 0.7137255f);
		colors[212] = new Color3f(1.0f, 0.7294118f, 0.7137255f);
		colors[213] = new Color3f(1.0f, 0.73333335f, 0.7254902f);
		colors[214] = new Color3f(1.0f, 0.7372549f, 0.7254902f);
		colors[215] = new Color3f(1.0f, 0.7411765f, 0.7372549f);
		colors[216] = new Color3f(1.0f, 0.7411765f, 0.7372549f);
		colors[217] = new Color3f(1.0f, 0.74509805f, 0.7372549f);
		colors[218] = new Color3f(1.0f, 0.7490196f, 0.7490196f);
		colors[219] = new Color3f(1.0f, 0.7529412f, 0.7490196f);
		colors[220] = new Color3f(1.0f, 0.7607843f, 0.7607843f);
		colors[221] = new Color3f(1.0f, 0.7607843f, 0.7607843f);
		colors[222] = new Color3f(1.0f, 0.77254903f, 0.77254903f);
		colors[223] = new Color3f(1.0f, 0.7764706f, 0.7764706f);
		colors[224] = new Color3f(1.0f, 0.78431374f, 0.78431374f);
		colors[225] = new Color3f(1.0f, 0.7882353f, 0.7882353f);
		colors[226] = new Color3f(1.0f, 0.7882353f, 0.7882353f);
		colors[227] = new Color3f(1.0f, 0.7921569f, 0.7921569f);
		colors[228] = new Color3f(1.0f, 0.79607844f, 0.79607844f);
		colors[229] = new Color3f(1.0f, 0.8039216f, 0.8039216f);
		colors[230] = new Color3f(1.0f, 0.80784315f, 0.80784315f);
		colors[231] = new Color3f(1.0f, 0.80784315f, 0.80784315f);
		colors[232] = new Color3f(1.0f, 0.8156863f, 0.8156863f);
		colors[233] = new Color3f(1.0f, 0.81960785f, 0.81960785f);
		colors[234] = new Color3f(1.0f, 0.827451f, 0.827451f);
		colors[235] = new Color3f(1.0f, 0.84313726f, 0.84313726f);
		colors[236] = new Color3f(1.0f, 0.84705883f, 0.84705883f);
		colors[237] = new Color3f(1.0f, 0.84705883f, 0.84705883f);
		colors[238] = new Color3f(1.0f, 0.85490197f, 0.85490197f);
		colors[239] = new Color3f(1.0f, 0.85882354f, 0.85882354f);
		colors[240] = new Color3f(1.0f, 0.8666667f, 0.8666667f);
		colors[241] = new Color3f(1.0f, 0.8745098f, 0.8745098f);
		colors[242] = new Color3f(1.0f, 0.8862745f, 0.8862745f);
		colors[243] = new Color3f(1.0f, 0.89411765f, 0.89411765f);
		colors[244] = new Color3f(1.0f, 0.9019608f, 0.9019608f);
		colors[245] = new Color3f(1.0f, 0.9019608f, 0.9019608f);
		colors[246] = new Color3f(1.0f, 0.9098039f, 0.9098039f);
		colors[247] = new Color3f(1.0f, 0.92156863f, 0.92156863f);
		colors[248] = new Color3f(1.0f, 0.92941177f, 0.92941177f);
		colors[249] = new Color3f(1.0f, 0.9411765f, 0.9411765f);
		colors[250] = new Color3f(1.0f, 0.9529412f, 0.9529412f);
		colors[251] = new Color3f(1.0f, 0.9647059f, 0.9647059f);
		colors[252] = new Color3f(1.0f, 0.9764706f, 0.9764706f);
		colors[253] = new Color3f(1.0f, 0.9843137f, 0.9843137f);
		colors[254] = new Color3f(1.0f, 0.99215686f, 0.99215686f);
		colors[255] = new Color3f(1.0f, 1.0f, 1.0f);

	}

	@Override
	public Color3f generate(float value) {
		int index = (int) Math.floor(value * (colors.length - 1));
		return colors[index];
	}

	@Override
	public String getName() {
		return "Rainbow";
	}

}
